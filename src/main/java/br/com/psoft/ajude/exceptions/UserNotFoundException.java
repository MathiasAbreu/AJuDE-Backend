package br.com.psoft.ajude.exceptions;

import org.springframework.stereotype.Component;

@Component
public class UserNotFoundException extends UserException {

    public UserNotFoundException(String email) {
        super("Usuário não encontrado: " + email);
    }

    public UserNotFoundException() {

        super("Usuário não encontrado!");
    }
}
