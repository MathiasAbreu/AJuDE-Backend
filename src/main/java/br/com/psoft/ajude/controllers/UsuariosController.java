package br.com.psoft.ajude.controllers;

import br.com.psoft.ajude.entities.Campanha;
import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.UserAlreadyExistException;
import br.com.psoft.ajude.exceptions.UserException;
import br.com.psoft.ajude.exceptions.UserNotFoundException;
import br.com.psoft.ajude.services.JWTService;
import br.com.psoft.ajude.services.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class UsuariosController {

    private UsuariosService usuariosService;
    private JWTService jwtService;

    public UsuariosController(UsuariosService usuariosService, JWTService jwtService) {

        super();
        this.usuariosService = usuariosService;
        this.jwtService = jwtService;
    }

    @PostMapping("usuarios/add")
    public ResponseEntity<String> adicionaUsuario(@RequestBody Usuario usuario) {

        try {

            usuariosService.adicionaUsuario(usuario);
            return new ResponseEntity<String>("Bem vindo! ", HttpStatus.CREATED);

        } catch (UserAlreadyExistException uaee) {

            return new ResponseEntity<String>(uaee.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("usuarios")
    public ResponseEntity<Usuario> buscaUsuario(@RequestBody Usuario usuario) {

        Optional<Usuario> retornoUsuario = usuariosService.getUsuario(usuario.getEmail());
        if(retornoUsuario.isPresent())
            return new ResponseEntity<Usuario>(retornoUsuario.get(),HttpStatus.OK);
        return new ResponseEntity<Usuario>(new Usuario(null, null, null, 0, null),HttpStatus.NOT_FOUND);
    }
}
