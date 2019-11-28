package br.com.psoft.ajude.controllers;

import br.com.psoft.ajude.entities.Usuario;
import br.com.psoft.ajude.exceptions.UserAlreadyExistException;
import br.com.psoft.ajude.exceptions.UserException;
import br.com.psoft.ajude.services.JWTService;
import br.com.psoft.ajude.services.UsuariosService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(value = "Controle de Usuários da API")
@RestController
@RequestMapping("usuarios")
public class UsuariosController {

    private UsuariosService usuariosService;
    private JWTService jwtService;

    public UsuariosController(UsuariosService usuariosService, JWTService jwtService) {

        super();
        this.usuariosService = usuariosService;
        this.jwtService = jwtService;
    }

    @ApiOperation(value = "Adiciona um novo usuário ao sistema.", notes = "Adição de um novo Usuário. Recebe como parâmetro de entrada, " +
                          "um JSON contendo todos os dados básicos de um novo usuário. Se a adição for um sucesso, retorna uma mensagem de boas vindas, caso contrário retorna uma mensagem de erro.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Retorna uma mensagem de boas vindas confirmando o êxito da operação."),
            @ApiResponse(code = 409, message = "Não foi possivel adicionar o novo usuário pois o email dele já está cadastrado sistema."),
    })
    @RequestMapping(value = "/adiciona", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> adicionaUsuario(@ApiParam(value = "Novo Usuário.") @RequestBody Usuario usuario) {

        try {

            usuariosService.adicionaUsuario(usuario);
            return new ResponseEntity<String>("Bem vindo! ", HttpStatus.CREATED);

        } catch (UserException uaee) {

            return new ResponseEntity<String>(uaee.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @ApiOperation(value = "Busca um usuário no banco de dados do sistema.", notes = "Busca um usuário na base de dados no sistema. " +
                          "Recebe como parâmetro um JSON contendo o email do usuário de interesse. Retorna o usuário caso seja encontrado, ou retorna um Usuário nulo.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna com sucesso que o usuario foi encontrado e retornado sem eventuais problemas."),
            @ApiResponse(code = 404, message = "Não foi possível encontrar o usuário na base de dados.")
    })
    @RequestMapping(value = "/busca", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Usuario> buscaUsuario(@ApiParam(value = "Email do Usuário.") @RequestBody Usuario usuario) {

        Optional<Usuario> retornoUsuario = usuariosService.getUsuario(usuario.getEmail());
        if(retornoUsuario.isPresent())
            return new ResponseEntity<Usuario>(retornoUsuario.get(),HttpStatus.OK);

        return new ResponseEntity<Usuario>(new Usuario(),HttpStatus.NOT_FOUND);
    }
}
