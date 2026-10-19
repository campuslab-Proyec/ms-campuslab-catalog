package com.example.ms_campuslab_catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.ms_campuslab_catalog.entity.Resource;
import com.example.ms_campuslab_catalog.repository.ResourceRepository;

class ResourceServiceTest {

    private ResourceRepository repository;
    private ResourceService service;

    @BeforeEach
    void setUp() {
        repository = mock(ResourceRepository.class);
        service = new ResourceService(repository, mock(EventPublisherService.class));
        when(repository.save(any(Resource.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    private static Resource resource(String code, String name, int stock) {
        return Resource.builder().id(1L).code(code).name(name).category("EQUIPMENT").stock(stock).isAvailable(true).build();
    }

    @Test
    void actualizaLosCamposEditablesPeroNoElCodigo() {
        when(repository.findById(1L)).thenReturn(Optional.of(resource("EQ-OSC-01", "Osciloscopio", 10)));

        Resource changes = resource("OTRO-CODIGO", "Osciloscopio 200MHz", 4);
        changes.setCategory("SUPPLY");
        changes.setIsAvailable(false);

        Resource updated = service.updateResource(1L, changes).orElseThrow();

        assertThat(updated.getCode()).isEqualTo("EQ-OSC-01");
        assertThat(updated.getName()).isEqualTo("Osciloscopio 200MHz");
        assertThat(updated.getCategory()).isEqualTo("SUPPLY");
        assertThat(updated.getStock()).isEqualTo(4);
        assertThat(updated.getIsAvailable()).isFalse();
    }

    @Test
    void actualizarUnRecursoInexistenteDevuelveVacio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThat(service.updateResource(99L, resource("X", "X", 1))).isEmpty();
        verify(repository, never()).save(any());
    }

    @Test
    void eliminaSoloSiExiste() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.existsById(99L)).thenReturn(false);

        assertThat(service.deleteResource(1L)).isTrue();
        verify(repository).deleteById(1L);

        assertThat(service.deleteResource(99L)).isFalse();
        verify(repository, never()).deleteById(99L);
    }
}
