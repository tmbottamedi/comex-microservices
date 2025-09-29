package br.com.alura.comex.service;

import br.com.alura.comex.model.Categoria;
import br.com.alura.comex.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public void cadastrar(Categoria categoria) {
        repository.save(categoria);
    }
}
