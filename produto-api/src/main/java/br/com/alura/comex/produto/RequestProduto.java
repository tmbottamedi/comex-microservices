package br.com.alura.comex.produto;

import org.hibernate.validator.constraints.Length;

import br.com.alura.comex.categoria.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RequestProduto {

    @NotBlank(message = "O nome é obrigatório")
    @Length(min = 2, message = "O nome deve ter no mínimo 2 caracteres")
    private String nome;

    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser um valor positivo")
    private double preco;

    @NotNull(message = "A quantidade em estoque é obrigatória")
    private int quantidadeEmEstoque;

    @NotNull(message = "O ID da categoria é obrigatório")
    private Long categoriaId;

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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Produto toProduto(Categoria categoria) {
        Produto produto = new Produto();
        produto.setNome(this.nome);
        produto.setDescricao(this.descricao);
        produto.setPreco(this.preco);
        produto.setQuantidadeEmEstoque(this.quantidadeEmEstoque);
        produto.setCategoria(categoria);
        return produto;
    }
}