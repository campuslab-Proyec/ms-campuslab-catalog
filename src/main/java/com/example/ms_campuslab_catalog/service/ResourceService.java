package com.example.ms_campuslab_catalog.service;

import org.springframework.stereotype.Service;

import com.example.ms_campuslab_catalog.entity.Resource;
import com.example.ms_campuslab_catalog.repository.ResourceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final EventPublisherService eventPublisherService;

    public ResourceService(ResourceRepository resourceRepository, EventPublisherService eventPublisherService) {
        this.resourceRepository = resourceRepository;
        this.eventPublisherService = eventPublisherService;
    }

    public Resource createResource(Resource resource) {
        // 1. Guardar en la base de datos (MySQL)
        Resource savedResource = resourceRepository.save(resource);

        // Las notificaciones son best-effort: si RabbitMQ falla, el recurso ya quedó guardado
        // y no debe responderse un 500 al cliente.
        try {
            // 2. Disparar Notificación por Email
            eventPublisherService.sendEmailNotification(
                "admin@campuslab.edu",
                "Nuevo recurso creado",
                "Se ha registrado el recurso: " + savedResource.getName()
            );

            // 3. Generar ticket de preparación en laboratorio
            eventPublisherService.sendPreparationTicket(
                "Preparar espacio para nuevo recurso ID: " + savedResource.getId(),
                "RESOURCE_CREATION"
            );
        } catch (Exception e) {
            log.warn("No se pudieron publicar los eventos del recurso {}: {}", savedResource.getId(), e.getMessage());
        }

        return savedResource;
    }
}
