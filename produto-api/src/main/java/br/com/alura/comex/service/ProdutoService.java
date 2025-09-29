package br.com.alura.comex.service;

import br.com.alura.comex.model.Produto;
import br.com.alura.comex.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public void cadastrar(Produto obj) {
        repository.save(obj);
    }
}
