package br.com.alura.comex.categoria;

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
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final AuthClient authClient;

    public CategoriaController(CategoriaService categoriaService, AuthClient authClient) {
        this.categoriaService = categoriaService;
        this.authClient = authClient;
    }

    @PostMapping
    public ResponseEntity<Object> cadastra(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestBody @Valid RequestCategoria request, BindingResult result) {

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

        Categoria categoria = request.toCategoria();
        categoriaService.cadastrar(categoria);

        return new ResponseEntity<>(categoria, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> lista() {
        List<Categoria> categorias = categoriaService.listar();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }
}