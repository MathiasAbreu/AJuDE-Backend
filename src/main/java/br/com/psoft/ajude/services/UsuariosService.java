package br.com.psoft.ajude.services;

import br.com.psoft.ajude.daos.UsuariosRepository;
import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.UserAlreadyExistException;
import br.com.psoft.ajude.exceptions.UserException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuariosService {

    private UsuariosRepository<Usuario, String> usuariosDao;

    public UsuariosService(UsuariosRepository<Usuario, String> usuariosDao) {

        super();

        this.usuariosDao = usuariosDao;
    }

    public void adicionaUsuario(Usuario usuario) throws UserException {

        Optional<Usuario> verificaUsuario = usuariosDao.findById(usuario.getEmail());

        if(!verificaUsuario.isPresent())
            EmailService.service(usuariosDao.save(usuario));
        else
            throw new UserAlreadyExistException(usuario.getEmail());
    }

    public Optional<Usuario> getUsuario(String email) {

        return usuariosDao.findById(email);
    }
}
