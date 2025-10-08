package br.com.alura.comex.consumer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioFallbackRepository extends JpaRepository<UsuarioFallback, Long> {
    Optional<UsuarioFallback> findByNome(String nome);
}