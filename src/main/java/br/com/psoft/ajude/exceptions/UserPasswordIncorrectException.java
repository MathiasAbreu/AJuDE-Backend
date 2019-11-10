package br.com.psoft.ajude.exceptions;

import org.springframework.stereotype.Component;

@Component
public class UserPasswordIncorrectException extends UserException {

    public UserPasswordIncorrectException() {
        super("Senha incorreta!");
    }
}
