package br.com.psoft.ajude.services;

import br.com.psoft.ajude.daos.*;
import br.com.psoft.ajude.entities.*;
import br.com.psoft.ajude.exceptions.*;
import br.com.psoft.ajude.services.comparators.ComparatorCampanhaPorData;
import br.com.psoft.ajude.services.comparators.ComparatorCampanhaPorLikes;
import br.com.psoft.ajude.services.comparators.ComparatorCampanhaPorMeta;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CampanhasService {

    private DoacoesRepository<Doacao, Long> doacoesDao;
    private CurtidasRepository<Curtida, Long> curtidasDao;
    private UsuariosRepository<Usuario, String> usuariosDao;
    private CampanhasRepository<Campanha, Long> campanhasDao;
    private ComentariosRepository<Comentario, Long> comentariosRepository;

    public CampanhasService(DoacoesRepository<Doacao, Long> doacoesDao, CurtidasRepository<Curtida, Long> curtidasDao, CampanhasRepository<Campanha, Long> campanhasDao, UsuariosRepository<Usuario, String> usuariosDao, ComentariosRepository<Comentario, Long> comentariosRepository) {

        super();
        this.doacoesDao = doacoesDao;
        this.curtidasDao = curtidasDao;
        this.usuariosDao = usuariosDao;
        this.campanhasDao = campanhasDao;
        this.comentariosRepository = comentariosRepository;
    }

    public Campanha buscaCampanha(String identificadorURL) throws CampaignNotFoundException {

        List<Campanha> campanhas = campanhasDao.findAll();

        for(Campanha campanha: campanhas) {

            if(campanha.getIdentificadorURL().equals(identificadorURL)) {

                if(campanha.verificaVencimento()) {

                    return campanhasDao.findById(campanha.getId()).map(record -> {

                        record.setStatus(campanha.getStatus());
                        return campanhasDao.save(record);

                    }).get();
                }
                return campanha;
            }
        }

        throw new CampaignNotFoundException(identificadorURL);
    }

    public Campanha atualizaDescricaoCampanha(String usuario, String identificadorURL, String novaDescricao) throws CampaignException, UserException {

        Campanha campanha = buscaCampanha(identificadorURL);

        if(campanha.getUsuario().getEmail().equals(usuario)) {
            return campanhasDao.findById(campanha.getId()).map(record -> {

                System.out.println(novaDescricao);
                record.setDescricao(novaDescricao);
                return campanhasDao.save(record);
            }).get();
        }

        throw new UserNotAuthorizedForProcedure();
    }

    public Campanha atualizaStatusCampanha(String usuario, String identificadorURL, String novoStatus) throws CampaignException, UserException {

        Campanha campanha = buscaCampanha(identificadorURL);

        if(campanha.getUsuario().getEmail().equals(usuario)) {

            return campanhasDao.findById(campanha.getId()).map(record -> {

                record.setStatus(novoStatus);
                return campanhasDao.save(record);
            }).get();
        }

        throw new UserNotAuthorizedForProcedure();
    }

    public Campanha adicionaCampanha(Campanha campanha,String emailUser) throws UserNotFoundException {

        Optional<Usuario> user = usuariosDao.findById(emailUser);
        if(user.isPresent()) {

            campanha.setUsuario(user.get());
            campanha = campanhasDao.save(campanha);
            adicionaCampanhaNoUsuario(user.get(), campanha);
            return campanhasDao.save(campanha);
        }

        throw new UserNotFoundException();
    }

    private void adicionaCampanhaNoUsuario(Usuario usuario, Campanha campanha) {

        usuariosDao.findById(usuario.getEmail()).map(record -> {

            record.adicionaCampanha(campanha);
            return usuariosDao.save(record);
        });
    }

    public List<Campanha> buscarCampanhaPorSubstring(String substring) {

        List<Campanha> campanhas = campanhasDao.findAll();
        List<Campanha> retorno = new ArrayList<Campanha>();

        for(int i = 0; i < campanhas.size(); i++) {

            if((campanhas.get(i).getIdentificadorURL().contains(substring.toLowerCase())))
                retorno.add(campanhas.get(i));
        }

        return retorno;
    }

    public List<Campanha> buscarCampanhaPorSubstring(String substring, List<String> parametros) {

        List<Campanha> campanhas = buscarCampanhaPorSubstring(substring);
        List<Campanha> retorno = new ArrayList<Campanha>();

        for(int i = 0; i < campanhas.size(); i++) {

            if(campanhas.get(i).getStatus().equals("Ativa") || parametros.contains(campanhas.get(i).getStatus()))
                retorno.add(campanhas.get(i));
        }

        return retorno;
    }

    private Campanha addComentario(Campanha campanha, Comentario comentario) {

        return (campanhasDao.findById(campanha.getId()).map(record -> {

            record.adicionaComentario(comentario);
            return campanhasDao.save(record);
        })).get();
    }

    //[comentario,resposta -/- conteudo] + [idComentario]
    public Campanha adicionaComentario(String emailUser, String identificadorURL, List<String> parametros) throws CampaignException, CommentParametersInsufficientException {

        Optional<Usuario> user = usuariosDao.findById(emailUser);

        if(!user.isPresent())
            throw new CommentParametersInsufficientException();

        Campanha campanha = buscaCampanha(identificadorURL);

        Comentario comentario;
        if(parametros.get(0).equals("resposta"))
            comentario = comentariosRepository.save(new Comentario(parametros.get(1),campanha,user.get(),Long.parseLong(parametros.get(2))));
        else
            comentario = comentariosRepository.save(new Comentario(parametros.get(1),campanha,user.get()));

        return addComentario(campanha,comentario);
    }

    public Campanha adicionaLike(String emailUser, String identificadorURL) throws CampaignException, UserNotFoundException {

        Campanha campanha = buscaCampanha(identificadorURL);
        Optional<Usuario> usuario = usuariosDao.findById(emailUser);

        if(!usuario.isPresent())
            throw new UserNotFoundException();

        Curtida curtida = curtidasDao.save(new Curtida(campanha,usuario.get()));

        return (campanhasDao.findById(campanha.getId()).map(record -> {

            record.adicionaCurtida(curtida);
            return campanhasDao.save(record);

        })).get();
    }

    public Campanha deletaLike(String emailUsuario, String identificadorURL) throws CampaignException {

        Campanha campanha = buscaCampanha(identificadorURL);
        List<Curtida> curtidas = campanha.getCurtidas();

        for(int i = 0; i < curtidas.size(); i++) {

            Curtida curtida = curtidas.get(i);
            if(curtida.getUsuarioQCurtiu().getEmail().equals(emailUsuario)) {

                return (campanhasDao.findById(campanha.getId()).map(record -> {

                    record.deletaCurtida(emailUsuario);
                    curtidasDao.delete(curtida);
                    return campanhasDao.save(record);

                })).get();
            }
        }

        throw new CampaignException();
    }

    //idComentario
    public Campanha deletarComentario(String emailUser, String identificadorURL, Long idComentario) throws CampaignException, UserException {

        Campanha campanha = buscaCampanha(identificadorURL);

        List<Comentario> comentarios = campanha.getComentarios();
        for(int i = 0; i < comentarios.size(); i++) {

            Comentario comentario = comentarios.get(i);
            if(comentario.getIdComentario() == idComentario) {

                if(comentario.getUsuarioQComentou().getEmail().equals(emailUser))
                    throw new UserNotAuthorizedForProcedure();

                campanha = (campanhasDao.findById(campanha.getId()).map(record -> {

                    record.deletaComentario(comentario);
                    return campanhasDao.save(record);

                })).get();
                return revisaoCampanha(campanha);
            }
        }

        return campanha;
    }

    private Campanha revisaoCampanha(Campanha campanha) {

        List<Comentario> comentarios = campanha.getComentarios();
        for(int i = 0; i < comentarios.size(); i++) {

            if(!comentarios.get(i).getStatus()) {

                comentarios.remove(i);
                return revisaoCampanha(campanha);
            }
        }
        return campanha;
    }

    public Campanha realizarDoacao(String emailUsuario, String identificadorURL, Double valorDoado) throws CampaignException, UserException {

        Optional<Usuario> usuario = usuariosDao.findById(emailUsuario);

        if(!usuario.isPresent())
            throw new UserNotFoundException();

        Campanha campanha = buscaCampanha(identificadorURL);
        Doacao doacao = new Doacao(campanha,usuario.get(),valorDoado);

        doacao = doacoesDao.save(doacao);
        adicionarDoacaoNoUsuario(usuario.get(), doacao);
        return adicionarDoacaoNaCampanha(campanha,doacao);

    }

    private Campanha adicionarDoacaoNaCampanha(Campanha campanha, Doacao doacao) {

        return campanhasDao.findById(campanha.getId()).map(record -> {

            record.adicionaDoacao(doacao);
            return campanhasDao.save(record);

        }).get();
    }

    private void adicionarDoacaoNoUsuario(Usuario usuario, Doacao doacao) {

        usuariosDao.findById(usuario.getEmail()).map(record -> {

            record.adicionaDoacao(doacao);
            return usuariosDao.save(record);
        });
    }

    public List<Campanha> retornaCampanhasDoUsuario(String emailUser) throws UserException {

        Optional<Usuario> usuario = usuariosDao.findById(emailUser);

        if(!usuario.isPresent())
            throw new UserNotFoundException();

        return usuario.get().getCampanhas();
    }

    public List<Campanha> retornaCampanhasContribuidas(String emailUser) throws UserException {

        Optional<Usuario> usuario = usuariosDao.findById(emailUser);

        if(!usuario.isPresent())
            throw new UserNotFoundException();

        List<Doacao> doacoes = usuario.get().getDoacoes();
        List<Campanha> campanhas = new ArrayList<>();

        for(Doacao doacao : doacoes)
            campanhas.add(doacao.getCampanhaAlvo());

        return campanhas;
    }

    public List<Campanha> retornarCampanhasOrdenadas(String parametroOrdenacao) {

        Comparator<Campanha> comparator = escolheCriterio(parametroOrdenacao);

        List<Campanha> todasCampanhas = campanhasDao.findAll();

        if(todasCampanhas.size() > 0)
            todasCampanhas.sort(comparator);

        List<Campanha> retornoCampanhas = new ArrayList<>();
        for(int i = 0; i < 5 && i < todasCampanhas.size(); i++) {
            retornoCampanhas.add(todasCampanhas.get(i));
        }

        return retornoCampanhas;
    }

    private Comparator<Campanha> escolheCriterio(String parametroOrdenacao) {

        if(parametroOrdenacao.equals("data"))
            return new ComparatorCampanhaPorData();

        if(parametroOrdenacao.equals("likes"))
            return new ComparatorCampanhaPorLikes();

        return new ComparatorCampanhaPorMeta();
    }
}
