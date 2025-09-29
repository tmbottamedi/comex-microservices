package br.com.alura.comex.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.comex.security.DadosTokenJWT;
import br.com.alura.comex.usuario.Usuario;
import br.com.alura.comex.usuario.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager manager;

    private final TokenService tokenService;

    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager manager, TokenService tokenService, UsuarioService usuarioService) {
        this.manager = manager;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Object> efetuarLogin(@RequestBody @Valid RequestAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @PostMapping("/init")
    public HttpStatus inicializaNovoUsuario() {

        String login = "admin@gmail.com";
        String senha = "admin123";
        usuarioService.save(login, senha);

        return HttpStatus.CREATED;
    }
}