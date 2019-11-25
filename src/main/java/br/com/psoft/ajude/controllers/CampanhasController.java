package br.com.psoft.ajude.controllers;

import br.com.psoft.ajude.entities.Campanha;
import br.com.psoft.ajude.exceptions.*;
import br.com.psoft.ajude.services.CampanhasService;
import br.com.psoft.ajude.services.JWTService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "Controle de Campanhas da API")
@RestController
@RequestMapping("campanhas")
public class CampanhasController {

    private CampanhasService campanhasService;
    private JWTService jwtService;

    public CampanhasController(CampanhasService campanhasService, JWTService jwtService) {

        super();
        this.jwtService = jwtService;
        this.campanhasService = campanhasService;
    }

    @ApiOperation(value = "Busca uma campanha no sistema.", notes = "Busca de Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token recebido não é válido."),
            @ApiResponse(code = 404, message = "A campanha desejada não foi encontrada!")
    })
    @RequestMapping(value = "{identificadorURL}/busca", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Campanha> buscaCampanha(@ApiParam("Token de autorização válido!") @RequestHeader("Authorization") String header, @ApiParam("Identificador de URl único da campanha.") @PathVariable String identificadorURL) {

        try {

            if (jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Campanha>(campanhasService.buscaCampanha(identificadorURL), HttpStatus.OK);
            }
            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(new Campanha(), HttpStatus.UNAUTHORIZED);
        } catch (CampaignNotFoundException err) {

            return new ResponseEntity<>(new Campanha(), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Permite que o dono atualize a descrição de uma campanha sua.", notes = "Atualização de Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token de usuário recebido não é válido ou o usuário não tem permissão para acesso a tal funcionalidade."),
            @ApiResponse(code = 404, message = "A campanha não foi encontrada!")
    })
    @RequestMapping(value = "{identificadorURL}/descricao", method = RequestMethod.PUT, produces = "application/json", consumes = "application/text")
    public ResponseEntity atualizaDescricaoCampanha(@ApiParam("Token de autorização válido!") @RequestHeader("Authorization") String header,  @ApiParam("Identificador de URl único da campanha.") @PathVariable String identificadorURL, @ApiParam("Nova descrição da campanha.") @RequestBody String novaDescricao) {

        try {

            if(jwtService.usuarioExiste(header)) {

                campanhasService.atualizaDescricaoCampanha(jwtService.getUsuarioDoToken(header),identificadorURL,novaDescricao);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Permite que o dono atualize o status de uma campanha sua.", notes = "Atualização de Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token de usuário recebido não é válido ou o usuário não tem permissão para acesso a tal funcionalidade."),
            @ApiResponse(code = 404, message = "A campanha não foi encontrada!")
    })
    @RequestMapping(value = "{identificadorURL}/status", method = RequestMethod.PUT, produces = "application/json", consumes = "application/text")
    public ResponseEntity atualizaStatusCampanha(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL, @RequestBody String novoStatus) {

        try {

            if(jwtService.usuarioExiste(header)) {

                campanhasService.atualizaStatusCampanha(jwtService.getUsuarioDoToken(header),identificadorURL,novoStatus);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Retorna um ranking com 5 disciplinas ordenadas por um determinado criterio de ordenação.", notes = "Atualização de Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
    })
    @RequestMapping(value = "/buscaTotal", method = RequestMethod.GET, produces = "application/json", consumes = "application/text")
    public ResponseEntity<List<Campanha>> retornarCampanhas(@ApiParam("Critério de ordenação.") @RequestBody String parametroOrdenacao) {

        return new ResponseEntity<List<Campanha>>(campanhasService.retornarCampanhasOrdenadas(parametroOrdenacao), HttpStatus.OK);
    }

    @ApiOperation(value = "Adiciona uma nova campanha no sistema", notes = "Adição de Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token de usuário recebido não é válido ou o usuário não tem permissão para acesso a tal funcionalidade."),
    })
    @RequestMapping(value = "/adiciona", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Campanha> adicionaCampanha(@ApiParam("Token de Usuário válido.") @RequestHeader("Authorization") String header, @ApiParam("Campanha a ser adicionada.") @RequestBody Campanha campanha) {

        try {

            if (jwtService.usuarioExiste(header)) {

                return new ResponseEntity<>(campanhasService.adicionaCampanha(campanha, jwtService.getUsuarioDoToken(header)), HttpStatus.CREATED);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(new Campanha(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "Retorna uma listagem de campanhas que sejam identificadas a partir de uma certa substring.", notes = "Busca de Campanhas Válidas.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token de usuário recebido não é válido ou o usuário não tem permissão para acesso a tal funcionalidade."),
    })
    @RequestMapping(value = "/buscaSubstring", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public ResponseEntity<List<Campanha>> buscaCampanhaPorSubstring(@ApiParam("Token válido do usuário.") @RequestHeader("Authorization") String header, @ApiParam("Listagem de paramêtros para a busca.") @RequestBody List<String> parametrosBusca) {

        try {

            if(jwtService.usuarioExiste(header)) {

                if(parametrosBusca.size() == 1)
                    return new ResponseEntity<>(campanhasService.buscarCampanhaPorSubstring(parametrosBusca.get(0)),HttpStatus.OK);
                else
                    return new ResponseEntity<>(campanhasService.buscarCampanhaPorSubstring(parametrosBusca.get(0),parametrosBusca),HttpStatus.OK);

            }

            throw new UserNotFoundException();
        } catch (UserException userExc) {

            return new ResponseEntity<>(new ArrayList<Campanha>(),HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "Adiciona um comentário/ ou resposta em uma campanha.", notes = "Adição de comentário em Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token de usuário recebido não é válido ou o usuário não tem permissão para acesso a tal funcionalidade."),
            @ApiResponse(code = 404, message = "A campanha não foi encontrada!"),
            @ApiResponse(code = 406, message = "Os paramêtros recebidos para adicionar um novo comentário estão incompletos ou inválidos!")
    })
    @RequestMapping(value = "{identificadorURL}/addComentario", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity adicionarComentario(@ApiParam("Token válido de usuário.") @RequestHeader("Authorization") String header,@ApiParam("Identificador URL da campanha.") @PathVariable String identificadorURL,@ApiParam("Listagem de paramêtros para o comentário/resposta.") @RequestBody List<String> parametros) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<>(campanhasService.adicionaComentario(jwtService.getUsuarioDoToken(header),identificadorURL,parametros),HttpStatus.CREATED);
            }
            throw new UserNotFoundException();

        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
        } catch (CommentException err) {

            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @ApiOperation(value = "Deleta um comentário em uma campanha.", notes = "Remoção de comentário em Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token de usuário recebido não é válido ou o usuário não tem permissão para acesso a tal funcionalidade."),
            @ApiResponse(code = 404, message = "A campanha não foi encontrada!"),
    })
    @RequestMapping(value = "{identificadorURL}/delComentario", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
    public ResponseEntity deletarComentario(@ApiParam("Token válido de usuário.") @RequestHeader("Authorization") String header,@ApiParam("Identificador URL da campanha.") @PathVariable String identificadorURL, @ApiParam("Id do comentário a ser deletado.") @RequestBody String idComentario) {

        try {

            if (jwtService.usuarioExiste(header)) {

                campanhasService.deletarComentario(jwtService.getUsuarioDoToken(header), identificadorURL, idComentario);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.UNAUTHORIZED);

        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Adiciona um like em uma campanha.", notes = "Adição de Like em Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token de usuário recebido não é válido ou o usuário não tem permissão para acesso a tal funcionalidade."),
            @ApiResponse(code = 404, message = "A campanha não foi encontrada!"),
    })
    @RequestMapping(value = "{identificadorURL}/addLike", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Campanha> adicionarLike(@ApiParam("Token do Usuário que deu o like.") @RequestHeader("Authorization") String header,@ApiParam("Campanha alvo do Like.") @PathVariable String identificadorURL) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Campanha>(campanhasService.adicionaLike(jwtService.getUsuarioDoToken(header),identificadorURL),HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch(UserException err) {

            return new ResponseEntity<>(new Campanha(),HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(new Campanha(), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Deleta um like em uma campanha.", notes = "Remoção de Like em Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token de usuário recebido não é válido ou o usuário não tem permissão para acesso a tal funcionalidade."),
            @ApiResponse(code = 404, message = "A campanha não foi encontrada!"),
    })
    @RequestMapping(value = "{identificadorURL}/delLike", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
    public ResponseEntity removerLike(@ApiParam("Token do Usuário que está desfazendo o like.") @RequestHeader("Authorization") String header,@ApiParam("Identificador da campanha que vai perder o like.") @PathVariable String identificadorURL) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Campanha>(campanhasService.deletaLike(jwtService.getUsuarioDoToken(header), identificadorURL), HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @ApiOperation(value = "Realiza uma doação para uma campanha especifica.", notes = "Adição de uma Doação em Campanha Válida.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna status de sucesso confirmando o êxito da operação."),
            @ApiResponse(code = 401, message = "O token de usuário recebido não é válido ou o usuário não tem permissão para acesso a tal funcionalidade."),
            @ApiResponse(code = 404, message = "A campanha não foi encontrada!"),
    })
    @RequestMapping(value = "{identificadorURL}/doacao", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity realizarDoacao(@ApiParam("Token do Usuário que está fazendo a doação.") @RequestHeader("Authorization") String header,@ApiParam("Campanha alvo da doação.") @PathVariable String identificadorURL,@ApiParam("Valor da doação.") @RequestBody Double doacao) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Double>(campanhasService.realizarDoacao(jwtService.getUsuarioDoToken(header), identificadorURL, doacao), HttpStatus.CREATED);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
