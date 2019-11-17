package br.com.psoft.ajude.controllers;

import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.UserException;
import br.com.psoft.ajude.exceptions.UserNotFoundException;
import br.com.psoft.ajude.exceptions.UserPasswordIncorrectException;
import br.com.psoft.ajude.services.JWTService;
import br.com.psoft.ajude.services.UsuariosService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
public class LoginController {

    private final String TOKEN_KEY = "DefaultUserLogin";

    private UsuariosService usuariosService;
    private JWTService jwtService;

    public LoginController(UsuariosService usuariosService, JWTService jwtService) {

        super();
        this.usuariosService = usuariosService;
        this.jwtService = jwtService;
    }

    @PostMapping("usuarios/login")
    public LoginResponse authenticate(@RequestBody Usuario usuario) throws UserException {

        Optional<Usuario> authUsuario = usuariosService.getUsuario(usuario.getEmail());

        if(!authUsuario.isPresent())
            throw new UserNotFoundException(authUsuario.get().getEmail());

        if(!authUsuario.get().getSenha().equals(usuario.getSenha()))
            throw new UserPasswordIncorrectException();

        String token = Jwts.builder().setSubject(authUsuario.get().getEmail()).signWith(SignatureAlgorithm.HS512,TOKEN_KEY).setExpiration(new Date(System.currentTimeMillis() + 3600 * 10000)).compact();
        return new LoginResponse(token);
    }

    private class LoginResponse {

        public String token;

        public LoginResponse(String token) {
            this.token = token;
        }
    }
}
