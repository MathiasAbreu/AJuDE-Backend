package br.com.psoft.ajude.controllers;

import br.com.psoft.ajude.entities.Campanha;
import br.com.psoft.ajude.exceptions.*;
import br.com.psoft.ajude.services.CampanhasService;
import br.com.psoft.ajude.services.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("{identificadorURL}/busca")
    public ResponseEntity buscaCampanha(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL) {

        try {

            if (jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Campanha>(campanhasService.buscaCampanha(identificadorURL), HttpStatus.OK);
            }
            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CampaignNotFoundException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{identificadorURL}/descricao")
    public ResponseEntity atualizaDescricaoCampanha(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL, @RequestBody String novaDescricao) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<>(campanhasService.atualizaDescricaoCampanha(jwtService.getUsuarioDoToken(header),identificadorURL,novaDescricao), HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{identificadorURL}/data")
    public ResponseEntity atualizaDataCampanha(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL, @RequestBody String novaData) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<>(campanhasService.atualizaDataCampanha(jwtService.getUsuarioDoToken(header),identificadorURL,novaData), HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{identificadorURL}/status")
    public ResponseEntity atualizaStatusCampanha(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL, @RequestBody String novoStatus) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<>(campanhasService.atualizaStatusCampanha(jwtService.getUsuarioDoToken(header),identificadorURL,novoStatus), HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{identificadorURL}/meta")
    public ResponseEntity atualizaMetaCampanha(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL, @RequestBody Double novaMeta) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<>(campanhasService.atualizaMetaCampanha(jwtService.getUsuarioDoToken(header),identificadorURL,novaMeta), HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/adiciona")
    public ResponseEntity adicionaCampanha(@RequestHeader("Authorization") String header, @RequestBody Campanha campanha) {

        try {

            if (jwtService.usuarioExiste(header)) {

                return new ResponseEntity<>(campanhasService.adicionaCampanha(campanha, jwtService.getUsuarioDoToken(header)), HttpStatus.CREATED);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/buscaSubstring")
    public ResponseEntity buscaCampanhaPorSubstring(@RequestHeader("Authorization") String header, @RequestBody List<String> parametrosBusca) {

        try {

            if(jwtService.usuarioExiste(header)) {

                if(parametrosBusca.size() == 1)
                    return new ResponseEntity<>(campanhasService.buscarCampanhaPorSubstring(parametrosBusca.get(0)),HttpStatus.OK);
                else
                    return new ResponseEntity<>(campanhasService.buscarCampanhaPorSubstring(parametrosBusca.get(0),parametrosBusca),HttpStatus.OK);

            }

            throw new UserNotFoundException();
        } catch (UserException userExc) {

            return new ResponseEntity<>(userExc.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("{identificadorURL}/addComentario")
    public ResponseEntity adicionarComentario(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL, @RequestBody List<String> parametros) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<>(campanhasService.adicionaComentario(jwtService.getUsuarioDoToken(header),identificadorURL,parametros),HttpStatus.CREATED);
            }
            throw new UserNotFoundException();

        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CampaignException | CommentException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DeleteMapping("{identificadorURL}/delComentario")
    public ResponseEntity deletarComentario(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL, @RequestBody String idComentario) {

        try {

            if (jwtService.usuarioExiste(header)) {

                return new ResponseEntity<>(campanhasService.deletarComentario(jwtService.getUsuarioDoToken(header), identificadorURL, idComentario), HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.UNAUTHORIZED);

        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{identificadorURL}/addLike")
    public ResponseEntity adicionarLike(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Campanha>(campanhasService.adicionaLike(jwtService.getUsuarioDoToken(header),identificadorURL),HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch(UserException err) {

            return new ResponseEntity<>(err.getMessage(),HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{identificadorURL/delLike")
    public ResponseEntity removerLike(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL) {

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

    @PostMapping("{identificadorURL/doacao")
    public ResponseEntity realizarDoacao(@RequestHeader("Authorization") String header, @PathVariable String identificadorURL, @RequestBody Double doacao) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Double>(campanhasService.realizarDoacao(jwtService.getUsuarioDoToken(header), identificadorURL, doacao), HttpStatus.CREATED);
            }

            throw new UserNotFoundException();
        } catch (UserException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<>(err.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
