package br.com.psoft.ajude.services;

import br.com.psoft.ajude.daos.CampanhasRepository;
import br.com.psoft.ajude.daos.ComentariosRepository;
import br.com.psoft.ajude.daos.UsuariosRepository;
import br.com.psoft.ajude.entities.Campanha;
import br.com.psoft.ajude.entities.Comentario;
import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.CampaignException;
import br.com.psoft.ajude.exceptions.CampaignNotFoundException;
import br.com.psoft.ajude.exceptions.CommentParametersInsufficientException;
import br.com.psoft.ajude.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CampanhasService {

    private UsuariosRepository<Usuario, String> usuariosDao;
    private CampanhasRepository<Campanha, Long> campanhasDao;
    private ComentariosRepository<Comentario, Long> comentariosRepository;

    public CampanhasService(CampanhasRepository<Campanha, Long> campanhasDao, UsuariosRepository<Usuario, String> usuariosDao, ComentariosRepository<Comentario, Long> comentariosRepository) {

        super();
        this.usuariosDao = usuariosDao;
        this.campanhasDao = campanhasDao;
        this.comentariosRepository = comentariosRepository;
    }

    public Campanha getCampanha(Campanha camp) throws CampaignNotFoundException {

        List<Campanha> campanhas = campanhasDao.findAll();

        for(Campanha campanha: campanhas) {

            if(campanha.getIdentificadorURL().equals(camp.getIdentificadorURL()))
                return campanha;
        }

        throw new CampaignNotFoundException(camp.getIdentificadorURL());
    }

    public void adicionaCampanha(Campanha campanha,String emailUser) throws UserNotFoundException {

        Optional<Usuario> user = usuariosDao.findById(emailUser);
        if(user.isPresent()) {

            campanha.setUsuario(user.get());
            campanhasDao.save(campanha);
        }
        else
            throw new UserNotFoundException(emailUser);
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

    //[comentario,resposta -/- conteudo -/- urlCampanha]
    public Campanha adicionaComentario(String emailUser, List<String> parametros) throws CampaignException,CommentParametersInsufficientException {

        Comentario comentario = new Comentario();
        comentario.setConteudo(parametros.get(1));

        Optional<Usuario> user = usuariosDao.findById(emailUser);

        if(!user.isPresent())
            throw new CommentParametersInsufficientException();

        comentario.setUsuario(user.get());

        Campanha campanha = new Campanha();
        campanha.setIdentificadorURL(parametros.get(2));

        campanha = getCampanha(campanha);

        System.out.println(campanha.toString());
        comentario.setCampanha(campanha);

        campanha = (campanhasDao.findById(campanha.getId()).map(record -> {

            System.out.println(comentario.toString());

            record.adicionaComentario(comentario);
            comentariosRepository.save(comentario);
            return campanhasDao.save(record);

        })).get();

        return campanha;
    }
    //[identificadorURL,idComentario]
    public void deletarComentario(String emailUser, List<String> parametros) throws CampaignNotFoundException {

        Campanha campanha = new Campanha();
        campanha.setIdentificadorURL(parametros.get(0));
        campanha = getCampanha(campanha);

        List<Comentario> comentarios = campanha.getComentarios();
        for(int i = 0; i < comentarios.size(); i++) {

            Comentario comentario = comentarios.get(i);
            if(comentario.getIdComentario() == Long.parseLong(parametros.get(1)) && comentario.getUsuario().getEmail().equals(emailUser)) {

                campanha = (campanhasDao.findById(campanha.getId()).map(record -> {

                    record.deletaComentario(comentario);
                    return campanhasDao.save(record);

                })).get();
                return;
            }
        }
    }

    /*private Campanha addResposta(String emailUser, List<String> parametros) throws CommentParametersInsufficientException, CampaignNotFoundException {

        Optional<Usuario> usuario = usuariosDao.findById(emailUser);

        if(!usuario.isPresent())
            throw new CommentParametersInsufficientException();

        Comentario resposta = new Comentario();
        resposta.setConteudo(parametros.get(0));
        resposta.setUsuario(usuario.get());

        Campanha campanha = new Campanha();
        campanha.setIdentificadorURL(parametros.get(1));

        campanha = getCampanha(campanha);
        campanha = (campanhasDao.findById(campanha.getId()).map(record -> {

            record.adicionaResposta(Long.parseLong(parametros.get(1)),resposta);
            return campanhasDao.save(record);
        })).get();

        return campanha;
    }*/
}
