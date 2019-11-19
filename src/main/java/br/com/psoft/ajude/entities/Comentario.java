package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Comentario {

    @Id @GeneratedValue
    private long idComentario;

    private String conteudo;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idResposta")
    private Comentario resposta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email")
    @JsonIgnore
    private Usuario usuario;

    @JsonCreator
    public Comentario(long idComentario, String conteudo) {

        super();
        this.conteudo = conteudo;
    }

    @JsonCreator
    public Comentario() {
        super();
    }

    public long getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(long idComentario) {
        this.idComentario = idComentario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Comentario getResposta() {
        return resposta;
    }

    public void setResposta(Comentario resposta) {
        this.resposta = resposta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
