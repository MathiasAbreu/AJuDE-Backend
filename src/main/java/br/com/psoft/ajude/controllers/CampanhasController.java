package br.com.psoft.ajude.controllers;

import br.com.psoft.ajude.entities.Campanha;
import br.com.psoft.ajude.entities.Comentario;
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

    @GetMapping("/busca")
    public ResponseEntity<Campanha> getCampanha(@RequestHeader("Authorization") String header, @RequestBody Campanha campanha) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Campanha>(campanhasService.getCampanha(campanha), HttpStatus.OK);
            }
            return new ResponseEntity<Campanha>(new Campanha(), HttpStatus.NON_AUTHORITATIVE_INFORMATION);

        } catch (UserException | CampaignNotFoundException userErr) {

            return new ResponseEntity<Campanha>(new Campanha(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/adiciona")
    public ResponseEntity<String> adicionaCampanha(@RequestHeader("Authorization") String header, @RequestBody Campanha campanha) {

        try {

            if(jwtService.usuarioExiste(header)) {

                campanhasService.adicionaCampanha(campanha,jwtService.getUsuarioDoToken(header));
                return new ResponseEntity<String>("Campanha cadastrada!", HttpStatus.CREATED);
            }

            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } catch (Exception exc) {

            return new ResponseEntity<String>(exc.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/buscaSubstring")
    public ResponseEntity<List<Campanha>> buscaCampanhaPorSubstring(@RequestHeader("Authorization") String header, @RequestBody List<String> parametrosBusca) {

        try {

            if(jwtService.usuarioExiste(header)) {

                if(parametrosBusca.size() == 1)
                    return new ResponseEntity<List<Campanha>>(campanhasService.buscarCampanhaPorSubstring(parametrosBusca.get(0)),HttpStatus.OK);
                else
                    return new ResponseEntity<List<Campanha>>(campanhasService.buscarCampanhaPorSubstring(parametrosBusca.get(0),parametrosBusca),HttpStatus.OK);

            }

            throw new UserNotFoundException();
        } catch (UserException userExc) {

            return new ResponseEntity<List<Campanha>>(new ArrayList<>(),HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping("/addComentario")
    public ResponseEntity<Campanha> adicionarComentario(@RequestHeader("Authorization") String header, @RequestBody List<String> parametros) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Campanha>(campanhasService.adicionaComentario(jwtService.getUsuarioDoToken(header),parametros),HttpStatus.CREATED);
            }
            throw new UserNotFoundException();

        } catch (UserException err) {

            return new ResponseEntity<Campanha>(new Campanha(), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } catch (CampaignException | CommentException err) {

            return new ResponseEntity<Campanha>(new Campanha(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DeleteMapping("/delComentario")
    public ResponseEntity<Campanha> deletarComentario(@RequestHeader("Authorization") String header, @RequestBody List<String> parametros) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Campanha>(campanhasService.deletarComentario(jwtService.getUsuarioDoToken(header),parametros),HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserException | CampaignException err) {

            return new ResponseEntity<Campanha>(new Campanha(),HttpStatus.NOT_FOUND);
        }
    }

    /*@PostMapping("/addLike")
    public ResponseEntity<Campanha> adicionarLike(@RequestHeader("Authorization") String header, @RequestBody String identificadorURL) {

        try {

            if(jwtService.usuarioExiste(header)) {

                return new ResponseEntity<Campanha>(campanhasService.adicionaLike(jwtService.getUsuarioDoToken(header),identificadorURL),HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch(UserException err) {

            return new ResponseEntity<Campanha>(new Campanha(),HttpStatus.UNAUTHORIZED);
        } catch (CampaignException err) {

            return new ResponseEntity<Campanha>(new Campanha(), HttpStatus.NOT_FOUND);
        }
    }*/
}
