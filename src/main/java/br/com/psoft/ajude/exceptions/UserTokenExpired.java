package br.com.psoft.ajude.exceptions;

import org.springframework.stereotype.Component;

@Component
public class UserTokenExpired extends UserException {

    public UserTokenExpired() {
        super("Token expirado!");
    }
}
