package br.com.psoft.ajude.services;

import br.com.psoft.ajude.daos.CampanhasRepository;
import br.com.psoft.ajude.daos.UsuariosRepository;
import br.com.psoft.ajude.entities.Campanha;
import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.UserAlreadyExistException;
import br.com.psoft.ajude.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuariosService {

    private UsuariosRepository<Usuario, String> usuariosDao;
    private CampanhasRepository<Campanha, Long> campanhasDao;

    public UsuariosService(UsuariosRepository<Usuario, String> usuariosDao, CampanhasRepository<Campanha, Long> campanhasDao) {

        super();

        this.usuariosDao = usuariosDao;
        this.campanhasDao = campanhasDao;
    }

    public void adicionaUsuario(Usuario usuario) throws UserAlreadyExistException {

        Optional<Usuario> verificaUsuario = usuariosDao.findById(usuario.getEmail());

        if(!verificaUsuario.isPresent())
            usuariosDao.save(usuario);
        else
            throw new UserAlreadyExistException(usuario.getEmail());
    }

    public Optional<Usuario> getUsuario(String email) {

        return usuariosDao.findById(email);
    }

    public void adicionaCampanha(Campanha campanha, String emailUser) throws UserNotFoundException {

        Optional<Usuario> user = usuariosDao.findById(emailUser);
        if(user.isPresent()) {

            campanha.setUsuario(user.get());
            user.get().adicionaCampanha(campanha);
            campanhasDao.save(campanha);
        }

        throw new UserNotFoundException("Usuário não encontrado!");
    }
}
