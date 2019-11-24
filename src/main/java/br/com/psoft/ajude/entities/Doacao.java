package br.com.psoft.ajude.entities;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Doacao {

    @Id @GeneratedValue
    private long idDoacao;

    private double valorDoado;

    @ManyToOne
    @JoinColumn(name = "id")
    @JsonIgnore
    private Campanha campanhaAlvo;

    @ManyToOne
    @JoinColumn(name = "email")
    private Usuario usuarioDoador;

    @JsonCreator
    public Doacao(Long idDoacao, double valorDoado) {

        super();
        this.idDoacao = idDoacao;
        this.valorDoado = valorDoado;
    }

    @JsonCreator
    public Doacao() {

        super();
    }

    public Doacao(Campanha campanha, Usuario usuario, double valorDoado) {

        super();
        this.campanhaAlvo = campanha;
        this.usuarioDoador = usuario;
        this.valorDoado = valorDoado;
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
