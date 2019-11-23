package br.com.psoft.ajude.entities;


import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.*;

@Entity
public class Doacao {

    @Id @GeneratedValue
    private long idDoacao;

    private double valorDoado;

    @ManyToOne
    @JoinColumn(name = "id")
    private Campanha campanhaAlvo;

    @ManyToOne
    @JoinColumn(name = "email")
    private Usuario usuarioDoador;

    @JsonCreator
    public Doacao(Long idDoacao, double valorDoado, Campanha campanhaAlvo, Usuario usuarioDoador) {

        super();
        this.idDoacao = idDoacao;
        this.valorDoado = valorDoado;
        this.campanhaAlvo = campanhaAlvo;
        this.usuarioDoador = usuarioDoador;
    }

    @JsonCreator
    public Doacao() {

        super();
    }

    public long getIdDoacao() {
        return idDoacao;
    }

    public void setIdDoacao(long idDoacao) {
        this.idDoacao = idDoacao;
    }

    public double getValorDoado() {
        return valorDoado;
    }

    public void setValorDoado(double valorDoado) {
        this.valorDoado = valorDoado;
    }

    public Campanha getCampanhaAlvo() {
        return campanhaAlvo;
    }

    public void setCampanhaAlvo(Campanha campanhaAlvo) {
        this.campanhaAlvo = campanhaAlvo;
    }

    public Usuario getUsuarioDoador() {
        return usuarioDoador;
    }

    public void setUsuarioDoador(Usuario usuarioDoador) {
        this.usuarioDoador = usuarioDoador;
    }
}
