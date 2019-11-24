package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    private String email;

    private String nome;
    private String ultimoNome;

    private Long numeroCartao;

    private String senha;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Campanha> campanhas = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioDoador", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Doacao> doacoes = new ArrayList<>();

    @JsonCreator
    public Usuario(String email, String nome, String ultimoNome, long numeroCartao, String senha) {

        super();

        this.nome = nome;
        this.ultimoNome = ultimoNome;
        this.email = email;
        this.numeroCartao = numeroCartao;
        this.senha = senha;

    }

    @JsonCreator
    public Usuario() {

        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public long getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(long numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Campanha> getCampanhas() {
        return campanhas;
    }

    public void setCampanhas(List<Campanha> campanhas) {
        this.campanhas = campanhas;
    }

    public List<Doacao> getDoacoes() {
        return doacoes;
    }

    public void setDoacoes(List<Doacao> doacoes) {
        this.doacoes = doacoes;
    }

    public void adicionaCampanha(Campanha campanha) {
        campanhas.add(campanha);
    }

    public void adicionaDoacao(Doacao doacao) {
        doacoes.add(doacao);
    }
}
