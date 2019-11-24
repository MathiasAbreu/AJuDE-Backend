package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

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
    private double valorDoado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email")
    private Usuario usuario;

    @OneToMany(mappedBy = "campanha", fetch = FetchType.LAZY)
    private List<Comentario> comentarios = new ArrayList<>();

    @OneToMany(mappedBy = "campanhaAlvo", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Doacao> doacoes = new ArrayList<>();

    @OneToMany(mappedBy = "campanhaCurtida", fetch = FetchType.LAZY)
    private List<Curtida> curtidas = new ArrayList<>();

    @JsonCreator
    public Campanha(long id, String nome, String descricao, String identificadorURL, String dataDeadline, double meta) {

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.identificadorURL = identificadorURL;
        this.dataDeadline = dataDeadline;
        this.meta = meta;

        this.status = "Ativa";
        this.valorDoado = 0;
    }

    @JsonCreator
    public Campanha() {
        super();
    }

    public Campanha(String identificadorURL) {

        super();
        this.identificadorURL = identificadorURL;
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

    public double getValorDoado() {
        return valorDoado;
    }

    public void setValorDoado(double valorDoado) {
        this.valorDoado = valorDoado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public List<Curtida> getCurtidas() {
        return curtidas;
    }

    public List<Doacao> getDoacoes() {
        return doacoes;
    }

    public void adicionaComentario(Comentario comentario) {
        this.comentarios.add(comentario);
    }

    public void deletaComentario(Comentario comentario) {

        for(int i = 0; i < comentarios.size(); i++) {
            if(comentario.getIdComentario() == comentarios.get(i).getIdComentario()) {
                comentarios.get(i).setStatus(false);
                deletaResposta(comentario.getIdComentario());
                return;
            }
        }
    }

    private void deletaResposta(Long idComentario) {

        for(int i = 0; i < comentarios.size(); i++) {
            if(comentarios.get(i).getIdResposta() == idComentario) {
                comentarios.get(i).setStatus(false);
                return;
            }
        }
    }

    public void adicionaCurtida(Curtida curtida) {
        this.curtidas.add(curtida);
    }

    public void deletaCurtida(String emailUsuario) {

        for(int i = 0; i < curtidas.size(); i++) {

            if(curtidas.get(i).getUsuarioQCurtiu().getEmail().equals(emailUsuario)) {

                curtidas.remove(i);
                return;
            }
        }
    }

    public void adicionaDoacao(Doacao doacao) {
        this.doacoes.add(doacao);
    }

    public double valorRestante() {

        double retorno = 0;

        for(Doacao doacao : doacoes)
            retorno += doacao.getValorDoado();
        return (meta - retorno);
    }

    @Override
    public String toString() {
        return String.format("Campanha:\n ID: %d\n Nome: %s\n Descrição: %s\n Identificador URL: %s\n Data final: %s\n Meta: %.2f\n",id,nome,descricao,identificadorURL,dataDeadline,meta) + "[" + usuario.toString() + "]";
    }
}
