package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Entity;

@Entity
public class Comentario {

    private String conteudo;

    @JsonCreator
    public Comentario(String conteudo) {

        super();
        this.conteudo = conteudo;
    }

    @JsonCreator
    public Comentario() {
        super();
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
