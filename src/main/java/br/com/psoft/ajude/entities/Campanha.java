package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Campanha {

    @Id @GeneratedValue
    private long id;

    private String nome;
    private String descricao;
    private String identificadorURL;

    private Date dataDeadline;

    private String status;

    private double meta;
    private double doacoes;

    private String comentarios;
    private int likes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emailUsuario")
    @JsonIgnore
    private Usuario usuario;

    @JsonCreator
    public Campanha(long id, String nome, String descricao, String identificadorURL, Date dataDeadline, double meta, Usuario usuario) {

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.identificadorURL = identificadorURL;
        this.dataDeadline = dataDeadline;
        this.meta = meta;
        this.usuario = usuario;

        this.status = "Ativa.";
        this.doacoes = 0;

    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
