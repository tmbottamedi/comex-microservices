package br.com.alura.comex.auth;

import java.time.Instant;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.comex.producer.UsuarioFallback;
import br.com.alura.comex.producer.dto.MensagemEnum;
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

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_USERS = "usuario-login-exchange";
    private static final String RK_USERS_CREATED = "usuario-login";

    public AuthController(AuthenticationManager manager, TokenService tokenService, UsuarioService usuarioService,
            RabbitTemplate rabbitTemplate) {
        this.manager = manager;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<Object> efetuarLogin(@RequestBody @Valid RequestAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        rabbitTemplate.convertAndSend(EXCHANGE_USERS, RK_USERS_CREATED,
                new UsuarioFallback(dados.login(), tokenJWT, true, MensagemEnum.AUTH, Instant.now()));

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @PostMapping("/init")
    public HttpStatus inicializaNovoUsuario() {

        String login = "admin@gmail.com";
        String senha = "admin123";
        usuarioService.save(login, senha);

        return HttpStatus.CREATED;
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(
            @RequestHeader(name = "Authorization", required = true) String authorizationHeader) {
        try {
            String subject = tokenService.getSubject(authorizationHeader);
            if (subject != null) {
                return ResponseEntity.ok().body("Token é válido");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token é inválido");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token é inválido");
        }
    }
}