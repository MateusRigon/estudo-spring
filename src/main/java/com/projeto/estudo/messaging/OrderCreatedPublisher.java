package com.projeto.estudo.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderCreatedPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public OrderCreatedPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.order-created-queue}") String queueName
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    public void publish(OrderCreatedEvent payload) {
        rabbitTemplate.convertAndSend(queueName, payload);
    }
}
