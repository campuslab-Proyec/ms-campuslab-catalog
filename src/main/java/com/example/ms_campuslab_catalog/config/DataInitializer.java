package com.example.ms_campuslab_catalog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ms_campuslab_catalog.entity.Laboratory;
import com.example.ms_campuslab_catalog.repository.LaboratoryRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(LaboratoryRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(Laboratory.builder()
                        .code("LAB-CLOUD")
                        .name("Laboratorio Redes & Cloud")
                        .location("Edificio A - Piso 3")
                        .capacity(30)
                        .description("Equipado con servidores rack y switches Cisco.")
                        .isAvailable(true)
                        .build());

                repository.save(Laboratory.builder()
                        .code("LAB-ELEC")
                        .name("Laboratorio Electrónica")
                        .location("Edificio B - Piso 1")
                        .capacity(25)
                        .description("Osciloscopios, fuentes DC y kits Arduino/Raspberry Pi.")
                        .isAvailable(true)
                        .build());
            }
        };
    }
}