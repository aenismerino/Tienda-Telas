package com.resenas_service.resenas_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.resenas_service.resenas_service.DTO.ResenaDto;
import com.resenas_service.resenas_service.model.Resena;
import com.resenas_service.resenas_service.repository.ResenasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ResenasServiceTest {

    @InjectMocks
    private ResenasService resenasService;

    @Mock
    private ResenasRepository resenasRepository;

    private Resena resena;
    private ResenaDto resenaDto;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setId(1L);
        resena.setTelaId(10L);
        resena.setUsuarioId(5L);
        resena.setCalificacion(5);
        resena.setComentario("Excelente tela");
        resena.setFechaCreacion(LocalDate.now());

        resenaDto = new ResenaDto();
        resenaDto.setTelaId(1L);
        resenaDto.setTelaId(10L);
        resenaDto.setUsuarioId(5L);
        resenaDto.setCalificacion(5);
        resenaDto.setComentario("Excelente tela");
    }

    @Test
    void testListarTodas() {
        when(resenasRepository.findAll()).thenReturn(List.of(resena));
        List<Resena> result = resenasService.listarTodas();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testObtenerPorId() {
        when(resenasRepository.findById(1L)).thenReturn(Optional.of(resena));
        Resena result = resenasService.obtenerPorId(1L);
        assertNotNull(result);
        assertEquals("Excelente tela", result.getComentario());
    }

    @Test
    void testObtenerPorIdNoEncontrado() {
        when(resenasRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> resenasService.obtenerPorId(99L));
    }

    @Test
    void testCrearResena() {
        when(resenasRepository.existsByTelaIdAndUsuarioId(10L, 5L)).thenReturn(false);
        when(resenasRepository.save(any(Resena.class))).thenReturn(resena);

        Resena resultado = resenasService.crearResena(resenaDto);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());
        verify(resenasRepository).save(any(Resena.class));
    }

    @Test
    void testCrearResenaDuplicada() {
        when(resenasRepository.existsByTelaIdAndUsuarioId(10L, 5L)).thenReturn(true);
        assertThrows(RuntimeException.class, () -> resenasService.crearResena(resenaDto));
    }

    @Test
    void testEliminarResena() {
        doNothing().when(resenasRepository).deleteById(1L);
        assertDoesNotThrow(() -> resenasService.eliminarResena(1L));
        verify(resenasRepository, times(1)).deleteById(1L);
    }
}
