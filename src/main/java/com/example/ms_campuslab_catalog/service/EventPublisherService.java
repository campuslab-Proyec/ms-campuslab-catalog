package com.example.ms_campuslab_catalog.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.ms_campuslab_catalog.dto.NotificationMessageDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;
    public static final String EXCHANGE_DIRECT = "cmd.direct";

    public EventPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Publica hacia q.cmd.email
    public void sendEmailNotification(String recipient, String subject, String message) {
        NotificationMessageDTO dto = new NotificationMessageDTO(recipient, subject, message, "EMAIL_NOTIFY");
        rabbitTemplate.convertAndSend(EXCHANGE_DIRECT, "email.send", dto);
        log.info("Evento [email.send] enviado a RabbitMQ para: {}", recipient);
    }

    // Publica hacia q.cmd.prep
    public void sendPreparationTicket(String details, String eventType) {
        NotificationMessageDTO dto = new NotificationMessageDTO("OPERATIONS_TEAM", "PREP_REQUIRED", details, eventType);
        rabbitTemplate.convertAndSend(EXCHANGE_DIRECT, "prep.ticket", dto);
        log.info("Evento [prep.ticket] enviado a RabbitMQ: {}", details);
    }

    // Publica hacia q.cmd.voucher
    public void sendVoucherGeneration(String recipient, String voucherDetails) {
        NotificationMessageDTO dto = new NotificationMessageDTO(recipient, "VOUCHER_ISSUED", voucherDetails, "VOUCHER");
        rabbitTemplate.convertAndSend(EXCHANGE_DIRECT, "voucher.gen", dto);
        log.info("Evento [voucher.gen] enviado a RabbitMQ para: {}", recipient);
    }
}