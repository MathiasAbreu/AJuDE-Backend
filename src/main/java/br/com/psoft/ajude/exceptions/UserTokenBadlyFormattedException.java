package br.com.psoft.ajude.exceptions;

import org.springframework.stereotype.Component;

@Component
public class UserTokenBadlyFormattedException extends UserException {

    public UserTokenBadlyFormattedException() {
        super("Token do Usuário inexistente ou mal formatado!");
    }
}
