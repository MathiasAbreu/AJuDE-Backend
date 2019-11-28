package br.com.psoft.ajude.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Campanha {

    @ApiModelProperty(value = "Identificador de uma campanha")
    @Id @GeneratedValue
    private long id;

    @ApiModelProperty(value = "Nome de uma campanha")
    private String nome;

    @ApiModelProperty(value = "Descrição de Uma campanha")
    private String descricao;

    @ApiModelProperty(value = "Identificador URL único de uma campanha")
    private String identificadorURL;

    @ApiModelProperty(value = "Data para o fim de uma campanha")
    private String dataDeadline;

    @ApiModelProperty(value = "Status de uma campanha")
    private String status;

    @ApiModelProperty(value = "Meta de uma camapanha")
    private double meta;

    @ApiModelProperty(value = "Usuario dono da campanha")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email")
    private Usuario usuario;

    @ApiModelProperty(value = "Comentarios feitos na campanha")
    @OneToMany(mappedBy = "campanha", fetch = FetchType.LAZY)
    private List<Comentario> comentarios = new ArrayList<>();

    @ApiModelProperty(value = "Doacoes feitas na campanha")
    @OneToMany(mappedBy = "campanhaAlvo", fetch = FetchType.LAZY)
    private List<Doacao> doacoes = new ArrayList<>();

    @ApiModelProperty(value = "Likes dados para a campanha")
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public void setCurtidas(List<Curtida> curtidas) {
        this.curtidas = curtidas;
    }

    public void setDoacoes(List<Doacao> doacoes) {
        this.doacoes = doacoes;
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

        for(Curtida curtida : curtidas) {

            if(curtida.getUsuarioQCurtiu().getEmail().equals(emailUsuario)) {

                curtidas.remove(curtida);
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

    public int getQuantidadeLikes() {

        return curtidas.size();
    }

    public boolean verificaVencimento() {

        Date date = new Date(System.currentTimeMillis());
        String data = String.format("%d/%d/%d",date.getDay(),date.getMonth() + 1,date.getYear());

        if(data.compareTo(dataDeadline) == 1) {

            if(valorRestante() == 0)
                this.status = "Concluida";
            else
                this.status = "Vencida";

            return true;
        }
        return false;
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

}
