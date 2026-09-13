package com.example.ms_campuslab_catalog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ms_campuslab_catalog.entity.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByCode(String code);
}