package br.com.alura.comex.categoria;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestCategoria {

    @NotNull
    @NotBlank(message = "Campo não pode ser vazio")
    @Length(min = 2)
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria toCategoria() {
        return new Categoria(this.nome);
    }
}