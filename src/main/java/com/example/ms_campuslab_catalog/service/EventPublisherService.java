package com.example.ms_campuslab_catalog.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.ms_campuslab_catalog.dto.NotificationMessageDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;

    // Nombres centralizados en application.yaml (campuslab.rabbitmq.*)
    private final String exchange;
    private final String emailRoutingKey;
    private final String prepRoutingKey;
    private final String voucherRoutingKey;

    public EventPublisherService(RabbitTemplate rabbitTemplate,
                                 @Value("${campuslab.rabbitmq.exchange}") String exchange,
                                 @Value("${campuslab.rabbitmq.routing-keys.email}") String emailRoutingKey,
                                 @Value("${campuslab.rabbitmq.routing-keys.prep}") String prepRoutingKey,
                                 @Value("${campuslab.rabbitmq.routing-keys.voucher}") String voucherRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.emailRoutingKey = emailRoutingKey;
        this.prepRoutingKey = prepRoutingKey;
        this.voucherRoutingKey = voucherRoutingKey;
    }

    // Publica hacia q.cmd.email
    public void sendEmailNotification(String recipient, String subject, String message) {
        NotificationMessageDTO dto = new NotificationMessageDTO(recipient, subject, message, "EMAIL_NOTIFY");
        rabbitTemplate.convertAndSend(exchange, emailRoutingKey, dto);
        log.info("Evento [{}] enviado a RabbitMQ para: {}", emailRoutingKey, recipient);
    }

    // Publica hacia q.cmd.prep
    public void sendPreparationTicket(String details, String eventType) {
        NotificationMessageDTO dto = new NotificationMessageDTO("OPERATIONS_TEAM", "PREP_REQUIRED", details, eventType);
        rabbitTemplate.convertAndSend(exchange, prepRoutingKey, dto);
        log.info("Evento [{}] enviado a RabbitMQ: {}", prepRoutingKey, details);
    }

    // Publica hacia q.cmd.voucher
    public void sendVoucherGeneration(String recipient, String voucherDetails) {
        NotificationMessageDTO dto = new NotificationMessageDTO(recipient, "VOUCHER_ISSUED", voucherDetails, "VOUCHER");
        rabbitTemplate.convertAndSend(exchange, voucherRoutingKey, dto);
        log.info("Evento [{}] enviado a RabbitMQ para: {}", voucherRoutingKey, recipient);
    }
}
