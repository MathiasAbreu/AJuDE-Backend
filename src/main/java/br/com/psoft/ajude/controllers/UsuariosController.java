package br.com.psoft.ajude.controllers;

import br.com.psoft.ajude.entities.Campanha;
import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.UserAlreadyExistException;
import br.com.psoft.ajude.services.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController
public class UsuariosController {

    private UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {

        super();
        this.usuariosService = usuariosService;
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
        return new ResponseEntity<Usuario>(new Usuario(null, null, null, 0, null, null),HttpStatus.NOT_FOUND);
    }

    @PostMapping("campanhas/add")
    public ResponseEntity<String> adicionaCampanha(@RequestBody Campanha campanha, @RequestBody String emailUser) {

        try {

            usuariosService.adicionaCampanha(campanha,emailUser);
            return new ResponseEntity<String>("Campanha cadastrada!", HttpStatus.CREATED);

        } catch (Exception exc) {

            return new ResponseEntity<String>(exc.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
