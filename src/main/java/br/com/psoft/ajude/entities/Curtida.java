package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.*;

@Entity
public class Curtida {

    @Id @GeneratedValue
    private long idCurtida;

    @ManyToOne
    @JoinColumn(name = "id")
    private Campanha campanhaCurtida;

    @ManyToOne
    @JoinColumn(name = "email")
    private Usuario usuarioQCurtiu;

    @JsonCreator
    public Curtida(Long idCurtida, Campanha campanhaCurtida, Usuario usuarioQCurtiu) {

        super();
        this.idCurtida = idCurtida;
        this.campanhaCurtida = campanhaCurtida;
        this.usuarioQCurtiu = usuarioQCurtiu;
    }

    @JsonCreator
    public Curtida() {

        super();
    }

    public long getIdCurtida() {
        return idCurtida;
    }

    public void setIdCurtida(long idCurtida) {
        this.idCurtida = idCurtida;
    }

    public Campanha getCampanhaCurtida() {
        return campanhaCurtida;
    }

    public void setCampanhaCurtida(Campanha campanhaCurtida) {
        this.campanhaCurtida = campanhaCurtida;
    }

    public Usuario getUsuarioQCurtiu() {
        return usuarioQCurtiu;
    }

    public void setUsuarioQCurtiu(Usuario usuarioQCurtiu) {
        this.usuarioQCurtiu = usuarioQCurtiu;
    }
}
