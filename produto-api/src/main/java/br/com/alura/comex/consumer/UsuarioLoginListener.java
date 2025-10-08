package br.com.alura.comex.consumer;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UsuarioLoginListener {

    private final UsuarioFallbackRepository usuarioFallbackRepository;

    public UsuarioLoginListener(UsuarioFallbackRepository usuarioFallbackRepository) {
        this.usuarioFallbackRepository = usuarioFallbackRepository;
    }

    @RabbitListener(queues = QueueConfig.QUEUE_USERS_CREATED)
    public void onUserAction(UsuarioFallback event) {
        try {
            usuarioFallbackRepository.save(event);

        } catch (IllegalArgumentException e) {
            throw new AmqpRejectAndDontRequeueException("Erro de negócio: " + e.getMessage(), e);
        } catch (Exception e) {
            throw e;
        }
    }

}
