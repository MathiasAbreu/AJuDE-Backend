package br.com.psoft.ajude.exceptions;

public class UserNotAuthorizedForProcedure extends UserException {

    public UserNotAuthorizedForProcedure() {
        super("O usuário não possui permissão para acessar essa funcionalidade!");
    }
}
