package br.com.psoft.ajude.services;

import br.com.psoft.ajude.daos.UsuariosDAO;
import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.UserAlreadyExistException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuariosService {

    private UsuariosDAO<Usuario, String> usuariosDao;

    public UsuariosService(UsuariosDAO<Usuario, String> usuariosDao) {

        super();

        this.usuariosDao = usuariosDao;
    }

    public void adicionaUsuario(String nome, String ultimoNome, String email, long numeroCartao, int senhaCartao) throws UserAlreadyExistException {

        Optional<Usuario> verificaUsuario = usuariosDao.findById(email);

        if(!verificaUsuario.isPresent())
            usuariosDao.save(new Usuario(nome,ultimoNome,email,numeroCartao,senhaCartao));
        else
            throw new UserAlreadyExistException(email);
    }

    public Usuario getUsuario(String email) {

        Optional<Usuario> verificaUsuario = usuariosDao.findById(email);
        if(verificaUsuario.isPresent())
            return verificaUsuario.get();
        else
            return new Usuario();
    }
}
