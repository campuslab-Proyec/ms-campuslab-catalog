package com.example.ms_campuslab_catalog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ms_campuslab_catalog.entity.Laboratory;
import com.example.ms_campuslab_catalog.entity.Resource;
import com.example.ms_campuslab_catalog.repository.LaboratoryRepository;
import com.example.ms_campuslab_catalog.repository.ResourceRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(LaboratoryRepository labRepo, ResourceRepository resRepo) {
        return args -> {
            if (labRepo.count() == 0) {
                labRepo.save(Laboratory.builder()
                        .code("LAB-CLOUD")
                        .name("Laboratorio Redes & Cloud")
                        .location("Edificio A - Piso 3")
                        .capacity(30)
                        .description("Equipado con servidores rack y switches Cisco.")
                        .isAvailable(true)
                        .build());

                labRepo.save(Laboratory.builder()
                        .code("LAB-ELEC")
                        .name("Laboratorio Electrónica")
                        .location("Edificio B - Piso 1")
                        .capacity(25)
                        .description("Osciloscopios, fuentes DC y kits Arduino/Raspberry Pi.")
                        .isAvailable(true)
                        .build());
            }

            if (resRepo.count() == 0) {
                resRepo.save(Resource.builder()
                        .code("EQ-OSC-01")
                        .name("Osciloscopio Digital 100MHz")
                        .category("EQUIPMENT")
                        .stock(10)
                        .description("Osciloscopio Tektronix 2 canales.")
                        .isAvailable(true)
                        .build());

                resRepo.save(Resource.builder()
                        .code("SUP-ARD-01")
                        .name("Kit Arduino Uno R3")
                        .category("SUPPLY")
                        .stock(50)
                        .description("Kit para laboratorio de electrónica básica.")
                        .isAvailable(true)
                        .build());
            }
        };
    }
}