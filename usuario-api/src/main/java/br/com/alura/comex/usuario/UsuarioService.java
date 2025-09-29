package br.com.alura.comex.usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder encoder;

    public UsuarioService(PasswordEncoder encoder, UsuarioRepository usuarioRepository) {
        this.encoder = encoder;
        this.usuarioRepository = usuarioRepository;
    }

    public void save(String login, String senha) {
        usuarioRepository.save(new Usuario(login, encoder.encode(senha)));
    }

}