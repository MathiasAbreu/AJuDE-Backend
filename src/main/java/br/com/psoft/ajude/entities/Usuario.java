package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Usuario {

    @Id
    private String email;

    private String nome;
    private String ultimoNome;

    private long numeroCartao;
    private int senhaCartao;

    @JsonCreator
    public Usuario(String nome, String ultimoNome, String email, long numeroCartao, int senhaCartao) {

        super();

        this.nome = nome;
        this.ultimoNome = ultimoNome;
        this.email = email;
        this.numeroCartao = numeroCartao;
        this.senhaCartao = senhaCartao;

    }

    @JsonCreator
    public Usuario() {
        super();
    }
}
