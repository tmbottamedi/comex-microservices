package br.com.alura.comex.produto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.comex.auth.client.AuthClient;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final AuthClient authClient;

    public ProdutoController(ProdutoService produtoService, AuthClient authClient) {
        this.produtoService = produtoService;
        this.authClient = authClient;
    }

    @PostMapping
    public ResponseEntity<Object> cadastra(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestBody @Valid RequestProduto request, BindingResult result) {

        if (result.hasErrors()) {
            String mensagem = result.getFieldError("nome").getDefaultMessage();
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token JWT ausente ou mal formatado.", HttpStatus.BAD_REQUEST);
        }

        String bearerToken = authorizationHeader.substring(7);

        if (authClient.validateToken(bearerToken).equals("Token é inválido")) {
            return new ResponseEntity<>("Sem autorização", HttpStatus.FORBIDDEN);
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
