package br.com.psoft.ajude.controllers;

import br.com.psoft.ajude.entities.Campanha;
import br.com.psoft.ajude.exceptions.UserException;
import br.com.psoft.ajude.exceptions.UserNotFoundException;
import br.com.psoft.ajude.services.CampanhasService;
import br.com.psoft.ajude.services.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CampanhasController {

    private CampanhasService campanhasService;
    private JWTService jwtService;

    public CampanhasController(CampanhasService campanhasService, JWTService jwtService) {

        super();
        this.jwtService = jwtService;
        this.campanhasService = campanhasService;
    }

    @GetMapping("campanhas/busca")
    public ResponseEntity<String> getCampanha(@RequestBody Campanha campanha) {

        return new ResponseEntity<String>(campanhasService.getCampanha(campanha), HttpStatus.OK);
    }

    @PostMapping("campanhas/adicionar")
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

    @GetMapping("campanhas/buscaPorSubstring")
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

            return new ResponseEntity<List<Campanha>>(new ArrayList<>(),HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
