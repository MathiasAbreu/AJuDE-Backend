package br.com.psoft.ajude.controllers;

import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.UserAlreadyExistException;
import br.com.psoft.ajude.services.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuariosController {

    private UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {

        super();
        this.usuariosService = usuariosService;
    }

    @PostMapping("usuarios")
    public ResponseEntity<String> adicionaUsuario(@RequestBody String nome, @RequestBody String ultimoNome, @RequestBody String email, @RequestBody Long numeroCartao, @RequestBody Integer senhaCartao) {

        try {

            usuariosService.adicionaUsuario(nome,ultimoNome,email,numeroCartao,senhaCartao);
            return new ResponseEntity<String>("Bem vindo! ", HttpStatus.CREATED);

        } catch (UserAlreadyExistException uaee) {

            return new ResponseEntity<String>(uaee.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("usuarios")
    public ResponseEntity<Usuario> buscaUsuario(@RequestBody String email) {

        return new ResponseEntity<Usuario>(usuariosService.getUsuario(email),HttpStatus.OK);
    }
}
