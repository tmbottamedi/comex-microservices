package br.com.alura.comex.consumer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class QueueConfig {

    public static final String EXCHANGE_USERS = "usuario-login-exchange";
    public static final String QUEUE_USERS_CREATED = "usuario-login-q";
    public static final String QUEUE_USERS_CREATED_DLQ = "usuario-login-dlq";
    public static final String RK_USERS_CREATED = "usuario-login";

    @Bean
    TopicExchange usersExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_USERS).durable(true).build();
    }

    @Bean
    Queue usersCreatedQueue() {
        return QueueBuilder.durable(QUEUE_USERS_CREATED)
                .withArgument("x-dead-letter-exchange", EXCHANGE_USERS)
                .withArgument("x-dead-letter-routing-key", RK_USERS_CREATED + ".dlq")
                .build();
    }

    @Bean
    Queue usersCreatedDlq() {
        return QueueBuilder.durable(QUEUE_USERS_CREATED_DLQ).build();
    }

    @Bean
    Binding usersCreatedBinding() {
        return BindingBuilder.bind(usersCreatedQueue())
                .to(usersExchange())
                .with(RK_USERS_CREATED);
    }

    @Bean
    Binding usersCreatedDlqBinding() {
        return BindingBuilder.bind(usersCreatedDlq())
                .to(usersExchange())
                .with(RK_USERS_CREATED + ".dlq");
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);

        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(4);
        factory.setPrefetchCount(50);
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}