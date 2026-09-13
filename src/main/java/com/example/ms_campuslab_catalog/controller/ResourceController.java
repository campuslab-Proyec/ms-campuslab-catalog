package com.example.ms_campuslab_catalog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_campuslab_catalog.entity.Resource;
import com.example.ms_campuslab_catalog.repository.ResourceRepository;
import com.example.ms_campuslab_catalog.service.ResourceService;

@RestController
@RequestMapping("/api/catalog/resources")
@CrossOrigin(origins = "*")
public class ResourceController {

    private final ResourceRepository repository;
    private final ResourceService resourceService;

    // Se inyecta tanto el repositorio (para lecturas) como el servicio (para el guardado con eventos)
    public ResourceController(ResourceRepository repository, ResourceService resourceService) {
        this.repository = repository;
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<Resource> getAllResources() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Resource> createResource(@RequestBody Resource resource) {
        // Ahora delegamos la creación al servicio para que dispare los eventos a RabbitMQ
        Resource saved = resourceService.createResource(resource);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Resource> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        return repository.findById(id)
                .map(existingResource -> {
                    existingResource.setStock(stock);
                    return ResponseEntity.ok(repository.save(existingResource));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}