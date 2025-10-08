package br.com.alura.comex.producer;

import java.time.Instant;

import br.com.alura.comex.producer.dto.MensagemEnum;

public class UsuarioFallback {

    private Long id;

    private String nome;

    private String token;

    private boolean ativo;

    private MensagemEnum tipo;

    private Instant momento;

    public UsuarioFallback(String nome, String token, boolean ativo, MensagemEnum tipo, Instant momento) {
        this.nome = nome;
        this.token = token;
        this.ativo = ativo;
        this.tipo = tipo;
        this.momento = momento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Instant getMomento() {
        return momento;
    }

    public void setMomento(Instant momento) {
        this.momento = momento;
    }

    public MensagemEnum getTipo() {
        return tipo;
    }

    public void setTipo(MensagemEnum tipo) {
        this.tipo = tipo;
    }
}