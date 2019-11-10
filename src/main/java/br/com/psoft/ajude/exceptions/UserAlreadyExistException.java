package br.com.psoft.ajude.exceptions;

import org.springframework.stereotype.Component;

@Component
public class UserAlreadyExistException extends UserException {

    public UserAlreadyExistException(String email) {
        super("Já existe um usuário cadastrado no email: " + email);
    }

    public UserAlreadyExistException() { super();}
}
