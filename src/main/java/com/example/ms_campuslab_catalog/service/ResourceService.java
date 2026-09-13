package com.example.ms_campuslab_catalog.service;

import org.springframework.stereotype.Service;

import com.example.ms_campuslab_catalog.entity.Resource;
import com.example.ms_campuslab_catalog.repository.ResourceRepository;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final EventPublisherService eventPublisherService;

    public ResourceService(ResourceRepository resourceRepository, EventPublisherService eventPublisherService) {
        this.resourceRepository = resourceRepository;
        this.eventPublisherService = eventPublisherService;
    }

    public Resource createResource(Resource resource) {
        // 1. Guardar en la base de datos (H2)
        Resource savedResource = resourceRepository.save(resource);

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

        return savedResource;
    }
}