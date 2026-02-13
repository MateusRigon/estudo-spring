package com.projeto.estudo.messaging;

import com.projeto.estudo.dto.ApiResponse;
import com.projeto.estudo.model.Client;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClientCreatedPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public ClientCreatedPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.client-created-queue}") String queueName
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    public void publish(ApiResponse<Client> payload) {
        rabbitTemplate.convertAndSend(queueName, payload);
    }
}
