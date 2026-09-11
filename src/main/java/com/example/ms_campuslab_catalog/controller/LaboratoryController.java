package com.example.ms_campuslab_catalog.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_campuslab_catalog.entity.Laboratory;
import com.example.ms_campuslab_catalog.repository.LaboratoryRepository;

@RestController
@RequestMapping("/api/catalog/labs")
@CrossOrigin(origins = "*")
public class LaboratoryController {

    private final LaboratoryRepository repository;

    public LaboratoryController(LaboratoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Laboratory> getAllLabs() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laboratory> getLabById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Laboratory> createLab(@RequestBody Laboratory lab) {
        Laboratory saved = repository.save(lab);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Laboratory> updateLab(@PathVariable Long id, @RequestBody Laboratory updated) {
        return repository.findById(id)
                .map(lab -> {
                    lab.setName(updated.getName());
                    lab.setLocation(updated.getLocation());
                    lab.setCapacity(updated.getCapacity());
                    lab.setDescription(updated.getDescription());
                    lab.setIsAvailable(updated.getIsAvailable());
                    return ResponseEntity.ok(repository.save(lab));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLab(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}