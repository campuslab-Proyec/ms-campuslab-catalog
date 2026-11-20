package com.example.ms_campuslab_catalog.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.ms_campuslab_catalog.entity.Resource;

@Component
public class KafkaCatalogProducer {

    public static final String TOPIC = "campuslab.catalog.events";
    private static final Logger log = LoggerFactory.getLogger(KafkaCatalogProducer.class);
    
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaCatalogProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /** Crear o modificar un recurso: publica su estado actual. */
    public void publishUpsert(Resource r) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("eventId", UUID.randomUUID().toString());
        event.put("type", "RESOURCE_UPSERT");
        event.put("code", r.getCode());
        event.put("name", r.getName());
        event.put("category", r.getCategory());
        event.put("stock", r.getStock());
        event.put("isAvailable", r.getIsAvailable());
        event.put("occurredAt", LocalDateTime.now().toString());
        
        send(r.getCode(), JsonUtil.toJson(event));
    }

    /** Eliminar un recurso: un mensaje con valor nulo (tombstone) borra la key al compactar. */
    public void publishDeleted(String code) {
        send(code, null);
    }

    private void send(String key, String value) {
        try {
            kafkaTemplate.send(TOPIC, key, value).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.warn("No se pudo publicar el evento del recurso {}: {}", key, ex.getMessage());
                } else {
                    log.info("Evento publicado: recurso={} partition={} offset={}", 
                            key, 
                            result.getRecordMetadata().partition(), 
                            result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.warn("Kafka no disponible, evento del recurso {} no publicado: {}", key, e.getMessage());
        }
    }
}
