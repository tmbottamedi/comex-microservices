package br.com.alura.comex.categoria;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Categoria categoria) {
        if (categoria == null)
            return;
        repository.save(categoria);
    }

    public List<Categoria> listar() {
        return repository.findAll();
    }
}