package br.com.psoft.ajude.exceptions;

public class CommentParametersInsufficientException extends CommentException {

    public CommentParametersInsufficientException() {
        super("Os parâmetros enviados são insuficientes para adicionar um comentário!");
    }

}
