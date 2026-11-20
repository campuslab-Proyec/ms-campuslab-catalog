package com.example.ms_campuslab_catalog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.example.ms_campuslab_catalog.service.KafkaCatalogProducer;
import com.example.ms_campuslab_catalog.service.ResourceService;

@RestController
@RequestMapping("/api/catalog/resources")
@CrossOrigin(origins = "*")
public class ResourceController {

    private final ResourceRepository repository;
    private final ResourceService resourceService;
    private final KafkaCatalogProducer kafkaProducer;

    public ResourceController(ResourceRepository repository,
                              ResourceService resourceService,
                              KafkaCatalogProducer kafkaProducer) {
        this.repository = repository;
        this.resourceService = resourceService;
        this.kafkaProducer = kafkaProducer;
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
        resource.setId(null); // Evita que un POST con id sobrescriba un registro existente
        Resource saved = resourceService.createResource(resource);
        kafkaProducer.publishUpsert(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateResource(@PathVariable Long id, @RequestBody Resource changes) {
        return resourceService.updateResource(id, changes)
                .map(updated -> {
                    kafkaProducer.publishUpsert(updated);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        Optional<String> code = repository.findById(id).map(Resource::getCode);
        if (resourceService.deleteResource(id)) {
            code.ifPresent(kafkaProducer::publishDeleted);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Resource> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        return repository.findById(id)
                .map(existingResource -> {
                    existingResource.setStock(stock);
                    Resource saved = repository.save(existingResource);
                    kafkaProducer.publishUpsert(saved);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}