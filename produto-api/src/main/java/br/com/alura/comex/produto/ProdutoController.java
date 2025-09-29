package br.com.alura.comex.produto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Object> cadastra(@RequestBody @Valid RequestProduto request, BindingResult result) {

        if (result.hasErrors()) {
            String mensagem = result.getFieldError("nome").getDefaultMessage();
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }
        Produto produto = produtoService.cadastrar(request);

        return new ResponseEntity<>(produto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> lista() {
        List<Produto> produtos = produtoService.listar();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }
}
