package com.projeto.estudo.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ClientCreatedConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCreatedConsumer.class);

    @RabbitListener(queues = "${app.rabbitmq.client-created-queue}")
    public void consume(Message message) {
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);
        String queueName = message.getMessageProperties().getConsumerQueue();
        LOGGER.info("Message consumed from queue '{}': {}", queueName, payload);
    }
}
