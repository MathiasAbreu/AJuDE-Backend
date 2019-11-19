package br.com.psoft.ajude.services;

import br.com.psoft.ajude.daos.CampanhasRepository;
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

    public CampanhasService(CampanhasRepository<Campanha, Long> campanhasDao, UsuariosRepository<Usuario, String> usuariosDao) {

        super();
        this.usuariosDao = usuariosDao;
        this.campanhasDao = campanhasDao;
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

            if(campanhas.get(i).getStatus().equals("Ativa.") || parametros.contains(campanhas.get(i).getStatus()))
                retorno.add(campanhas.get(i));
        }

        return retorno;
    }

    //[conteudo,{idDoComentario/urlCampanha},{Comentario/Resposta},
    public String adicionaComentario(String emailUser, List<String> parametros) throws CampaignException,CommentParametersInsufficientException {

        if(parametros.size() > 1) {

            if(parametros.get(2).equals("Comentario")) {

                addComentario(emailUser,parametros);
                return "Comentário adicionado com sucesso!";
            }

            if(parametros.get(2).equals("Resposta")) {

                addResposta(emailUser,parametros);
                return "Resposta adicionada com sucesso!";
            }

            throw new CommentParametersInsufficientException();
        }

        throw new CampaignException();
    }

    private void addComentario(String emailUser, List<String> parametros) throws CommentParametersInsufficientException, CampaignNotFoundException{

        Optional<Usuario> usuario = usuariosDao.findById(emailUser);

        if(!usuario.isPresent())
            throw new CommentParametersInsufficientException();

        Comentario comentario = new Comentario();
        comentario.setConteudo(parametros.get(0));
        comentario.setUsuario(usuario.get());

        Campanha campanha = new Campanha();
        campanha.setIdentificadorURL(parametros.get(1));

        campanha = getCampanha(campanha);
        campanha = (campanhasDao.findById(campanha.getId()).map(record -> {

            record.adicionaComentario(comentario);
            return campanhasDao.save(record);

        })).get();
    }

    private void addResposta(String emailUser, List<String> parametros) throws CommentParametersInsufficientException, CampaignNotFoundException {

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
    }
}
