package br.com.alura.comex.produto;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.alura.comex.categoria.Categoria;
import br.com.alura.comex.categoria.CategoriaRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Produto cadastrar(RequestProduto request) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(request.getCategoriaId());
        if (categoriaOptional.isEmpty()) {
            throw new IllegalArgumentException("ID de categoria inválido.");
        }
        Categoria categoria = categoriaOptional.get();
        Produto produto = request.toProduto(categoria);
        return produtoRepository.save(produto);
    }

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }
}