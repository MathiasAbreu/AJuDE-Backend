package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comentario {

    @Id @GeneratedValue
    private long idComentario;
    private long idResposta;  //Indica se esse comentário é uma resposta de outro comentario.

    private String conteudo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    @JsonIgnore
    private Campanha campanha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email")
    private Usuario usuarioQComentou;

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

    public Comentario(String conteudo, Campanha campanha, Usuario usuarioQComentou) {

        this.conteudo = conteudo;
        this.campanha = campanha;
        this.usuarioQComentou = usuarioQComentou;
        this.status = true;
    }

    public Comentario(String conteudo, Campanha campanha, Usuario usuarioQComentou, long idResposta) {

        this.conteudo = conteudo;
        this.campanha = campanha;
        this.usuarioQComentou = usuarioQComentou;
        this.status = true;
        this.idResposta = idResposta;
    }

    public long getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(long idComentario) {
        this.idComentario = idComentario;
    }

    public long getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(long resposta) {
        this.idResposta = resposta;
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

    public Usuario getUsuarioQComentou() {
        return usuarioQComentou;
    }

    public void setUsuarioQComentou(Usuario usuarioQComentou) {
        this.usuarioQComentou = usuarioQComentou;
    }

    public boolean getStatus() {
        return this.status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Id: %d, Conteudo: %s, URL: %s, Usuario: %s",idComentario,conteudo,campanha.getIdentificadorURL(), usuarioQComentou.getEmail());
    }
}
