package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Campanha {

    @Id @GeneratedValue
    private long id;

    private String nome;
    private String descricao;
    private String identificadorURL;

    private String dataDeadline;

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
    public Campanha(long id, String nome, String descricao, String identificadorURL, String dataDeadline, double meta, Usuario usuario) {

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

    @JsonCreator
    public Campanha() {

    }

    public String getIdentificadorURL() {
        return identificadorURL;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return String.format("Campanha:\n ID: %d\n Nome: %s\n Descrição: %s\n Identificador URL: %s\n Data final: %s\n Meta: %.2f\n",id,nome,descricao,identificadorURL,dataDeadline,meta) + "[" + usuario.toString() + "]";
    }
}
