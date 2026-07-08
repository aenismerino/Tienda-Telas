package com.resenas_service.resenas_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.resenas_service.resenas_service.DTO.ResenaDto;
import com.resenas_service.resenas_service.model.Resena;
import com.resenas_service.resenas_service.repository.ResenasRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResenasService {
    private final ResenasRepository resenaRepository;

    public List<Resena> listarTodas() {
        return resenaRepository.findAll();
    }

    public Resena obtenerPorId(Long id) {
        return resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
    }

    public Resena crearResena(ResenaDto dto) {
        log.info("Intentando crear reseña para telaId: {} por usuarioId {}", dto.getTelaId(), dto.getUsuarioId());
        if (resenaRepository.existsByTelaIdAndUsuarioId(dto.getTelaId(), dto.getUsuarioId())) {
            log.error("Falso al crear: El usuario {} ya reseño la tela {}", dto.getUsuarioId(), dto.getTelaId());
            throw new RuntimeException("Usuario ya creo una reseña para esta tela");
        }

        Resena resena = new Resena();
        resena.setTelaId(dto.getTelaId());
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        resena.setFechaCreacion(LocalDate.now());

        Resena guardada = resenaRepository.save(resena);
        log.info("Reseña creada exitosamente con ID: {}", guardada.getId());

        return guardada;
    }

    public void eliminarResena(Long id) {

        resenaRepository.deleteById(id);
        log.info("Reseña {} eliminada exitosamente", id);
    }

}
