package br.com.psoft.ajude.services;

import br.com.psoft.ajude.daos.CampanhasRepository;
import br.com.psoft.ajude.daos.ComentariosRepository;
import br.com.psoft.ajude.daos.UsuariosRepository;
import br.com.psoft.ajude.entities.Campanha;
import br.com.psoft.ajude.entities.Comentario;
import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CampanhasService {

    //private LikesRepository<Like, String> likesDao;
    private UsuariosRepository<Usuario, String> usuariosDao;
    private CampanhasRepository<Campanha, Long> campanhasDao;
    private ComentariosRepository<Comentario, Long> comentariosRepository;

    public CampanhasService(/*LikesRepository<Like, String> likesDao,*/ CampanhasRepository<Campanha, Long> campanhasDao, UsuariosRepository<Usuario, String> usuariosDao, ComentariosRepository<Comentario, Long> comentariosRepository) {

        super();
        //this.likesDao = likesDao;
        this.usuariosDao = usuariosDao;
        this.campanhasDao = campanhasDao;
        this.comentariosRepository = comentariosRepository;
    }

    public Campanha buscaCampanha(String identificadorURL) throws CampaignNotFoundException {

        List<Campanha> campanhas = campanhasDao.findAll();

        for(Campanha campanha: campanhas) {

            if(campanha.getIdentificadorURL().equals(identificadorURL))
                return campanha;
        }

        throw new CampaignNotFoundException(identificadorURL);
    }

    public Campanha atualizaDescricaoCampanha(String usuario, String identificadorURL, String novaDescricao) throws CampaignException, UserException {

        Campanha campanha = buscaCampanha(identificadorURL);

        if(campanha.getUsuario().getEmail().equals(usuario)) {

            return (campanhasDao.findById(campanha.getId()).map(record -> {

                System.out.println(novaDescricao);
                record.setDescricao(novaDescricao);
                return campanhasDao.save(record);
            })).get();
        }

        throw new UserNotAuthorizedForProcedure();
    }

    public Campanha atualizaStatusCampanha(String usuario, String identificadorURL, String novoStatus) throws CampaignException, UserException {

        Campanha campanha = buscaCampanha(identificadorURL);

        if(campanha.getUsuario().getEmail().equals(usuario)) {

            return (campanhasDao.findById(campanha.getId()).map(record -> {

                record.setStatus(novoStatus);
                return campanhasDao.save(record);
            })).get();
        }

        throw new UserNotAuthorizedForProcedure();
    }

    public Campanha atualizaMetaCampanha(String usuario, String identificadorURL, double novaMeta) throws CampaignException, UserException {

        Campanha campanha = buscaCampanha(identificadorURL);

        if(campanha.getUsuario().getEmail().equals(usuario)) {

            return (campanhasDao.findById(campanha.getId()).map(record -> {

                record.setMeta(novaMeta);
                return campanhasDao.save(record);
            })).get();
        }

        throw new UserNotAuthorizedForProcedure();
    }

    public Campanha atualizaDataCampanha(String usuario, String identificadorURL, String novaData) throws CampaignException, UserException {

        Campanha campanha = buscaCampanha(identificadorURL);

        if(campanha.getUsuario().getEmail().equals(usuario)) {

            return (campanhasDao.findById(campanha.getId()).map(record -> {

                record.setDataDeadline(novaData);
                return campanhasDao.save(record);
            })).get();
        }

        throw new UserNotAuthorizedForProcedure();
    }

    public Campanha adicionaCampanha(Campanha campanha,String emailUser) throws UserNotFoundException {

        Optional<Usuario> user = usuariosDao.findById(emailUser);
        if(user.isPresent()) {

            campanha.setUsuario(adicionaCampanhaNoUsuario(user.get(),campanha));
            return campanhasDao.save(campanha);
        }

        throw new UserNotFoundException();
    }

    private Usuario adicionaCampanhaNoUsuario(Usuario usuario, Campanha campanha) {

        return (usuariosDao.findById(usuario.getEmail()).map(record -> {

            record.adicionaCampanha(campanha);
            return usuariosDao.save(record);
        })).get();
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
            comentariosRepository.save(comentario);
            return campanhasDao.save(record);
        })).get();
    }

    private Campanha addResposta(Campanha campanha, Comentario comentario, Long idComentario) {

        return (campanhasDao.findById(campanha.getId()).map(record -> {

            comentario.setIdResposta(idComentario);
            record.adicionaComentario(comentario);
            comentariosRepository.save(comentario);
            return campanhasDao.save(record);
        })).get();
    }

    //[comentario,resposta -/- conteudo] + [idComentario]
    public Campanha adicionaComentario(String emailUser, String identificadorURL, List<String> parametros) throws CampaignException, CommentParametersInsufficientException {

        Optional<Usuario> user = usuariosDao.findById(emailUser);

        if(!user.isPresent())
            throw new CommentParametersInsufficientException();

        Campanha campanha = buscaCampanha(identificadorURL);

        Comentario comentario = new Comentario(parametros.get(1),campanha,user.get());

        if(parametros.get(0).equals("comentario"))
            return addComentario(campanha,comentario);

        if(parametros.get(0).equals("resposta"))
            return addResposta(campanha,comentario,Long.parseLong(parametros.get(2)));

        throw new CommentParametersInsufficientException();
    }

    /*public Campanha adicionaLike(String emailUser, String identificadorURL) throws CampaignException {

        Campanha campanha = new Campanha(identificadorURL);
        campanha = getCampanha(campanha);

        Like like = new Like(emailUser);

        return (campanhasDao.findById(campanha.getId()).map(record -> {

            //record.adicionaLike(like);
            likesDao.save(like);
            return campanhasDao.save(record);

        })).get();
    }*/
    //idComentario
    public Campanha deletarComentario(String emailUser, String identificadorURL, String idComentario) throws CampaignNotFoundException {

        Campanha campanha = buscaCampanha(identificadorURL);

        List<Comentario> comentarios = campanha.getComentarios();
        for(int i = 0; i < comentarios.size(); i++) {

            Comentario comentario = comentarios.get(i);
            if(comentario.getIdComentario() == Long.parseLong(idComentario) && comentario.getUsuarioQComentou().getEmail().equals(emailUser)) {

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
}
