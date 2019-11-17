package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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
    public Campanha(long id, String nome, String descricao, String identificadorURL, String dataDeadline, double meta) {

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.identificadorURL = identificadorURL;
        this.dataDeadline = dataDeadline;
        this.meta = meta;

        this.status = "Ativa";
        this.doacoes = 0;

    }

    @JsonCreator
    public Campanha() {

    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdentificadorURL() {
        return identificadorURL;
    }

    public void setIdentificadorURL(String identificadorURL) {
        this.identificadorURL = identificadorURL;
    }

    public String getDataDeadline() {
        return dataDeadline;
    }

    public void setDataDeadline(String dataDeadline) {
        this.dataDeadline = dataDeadline;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDoacoes() {
        return doacoes;
    }

    public void setDoacoes(double doacoes) {
        this.doacoes = doacoes;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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
