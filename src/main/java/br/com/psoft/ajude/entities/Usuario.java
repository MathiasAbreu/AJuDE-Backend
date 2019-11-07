package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Usuario {

    @Getter @Setter
    private String nome;
    @Getter @Setter
    private String ultimoNome;

    @Getter @Setter @Id
    private String email;

    @Getter @Setter
    private String numeroCartao;

    @Getter @Setter
    private String senha;

    @JsonCreator
    public Usuario(String nome, String ultimoNome, String email, String numeroCartao, String senha) {

        super();

        this.nome = nome;
        this.ultimoNome = ultimoNome;
        this.email = email;
        this.numeroCartao = numeroCartao;
        this.senha = senha;

    }

    public String getEmail() {
        return email;
    }
}
