package br.com.alura.comex.consumer.dto;

public record MensagemUsuario(String token,
        String nome,
        boolean ativo,
        MensagemEnum tipo) {

}
