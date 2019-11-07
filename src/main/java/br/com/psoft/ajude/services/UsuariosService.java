package br.com.psoft.ajude.services;

import br.com.psoft.ajude.daos.UsuariosRepository;
import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.UserAlreadyExistException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuariosService {

    private UsuariosRepository<Usuario, String> usuariosDao;

    public UsuariosService(UsuariosRepository<Usuario, String> usuariosDao) {

        super();

        this.usuariosDao = usuariosDao;
    }

    public void adicionaUsuario(Usuario usuario) throws UserAlreadyExistException {

        Optional<Usuario> verificaUsuario = usuariosDao.findById(usuario.getEmail());

        if(!verificaUsuario.isPresent())
            usuariosDao.save(usuario);
        else
            throw new UserAlreadyExistException(usuario.getEmail());
    }

    public Usuario getUsuario(String email) {

        Optional<Usuario> verificaUsuario = usuariosDao.findById(email);
        if(verificaUsuario.isPresent())
            return verificaUsuario.get();
        else
            return new Usuario(null,null,null,null,null);
    }
}
