package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Comentario {

    @Id @GeneratedValue
    private long idComentario;

    private String conteudo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    @JsonIgnore
    private Campanha campanha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email")
    @JsonIgnore
    private Usuario usuario;

    private boolean status;

    @JsonCreator
    public Comentario(long idComentario, String conteudo) {

        super();

        this.idComentario = idComentario;
        this.conteudo = conteudo;
        this.status = true;
    }

    @JsonCreator
    public Comentario() {
        super();
        this.status = true;
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

    public Campanha getCampanha() {
        return campanha;
    }

    public void setCampanha(Campanha campanha) {
        this.campanha = campanha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean getStatus() {
        return this.status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Id: %d, Conteudo: %s, URL: %s, Usuario: %s",idComentario,conteudo,campanha.getIdentificadorURL(),usuario.getEmail());
    }
}
