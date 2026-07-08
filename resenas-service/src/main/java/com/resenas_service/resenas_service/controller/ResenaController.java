package com.resenas_service.resenas_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.resenas_service.resenas_service.DTO.ResenaDto;
import com.resenas_service.resenas_service.model.Resena;
import com.resenas_service.resenas_service.service.ResenasService;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenasService resenaService;
    private static final Logger logger = LoggerFactory.getLogger(ResenaController.class);

    public ResenaController(ResenasService resenaService) {
        this.resenaService = resenaService;
    }

    @PostMapping
    public ResponseEntity<ResenaDto> crearResena(@Valid @RequestBody ResenaDto dto) {
        logger.info("V1 POST /resenas - Creando resena");
        Resena nuevaResena = resenaService.crearResena(dto);
        return ResponseEntity.ok(ResenaDto.fromModel(nuevaResena));
    }

    @GetMapping
    public ResponseEntity<List<ResenaDto>> listarResenas() {
        logger.info("V1 GET /resenas - Listando resenas");
        List<Resena> resenas = resenaService.listarTodas();
        List<ResenaDto> dtos = resenas.stream().map(ResenaDto::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenaDto> obtenerResena(@PathVariable Long id) {
        logger.info("V1 GET /resenas/{} - Obteniendo resena", id);
        Resena resena = resenaService.obtenerPorId(id);
        return ResponseEntity.ok(ResenaDto.fromModel(resena));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        logger.info("V1 DELETE /resenas/{} - Eliminando resena", id);
        resenaService.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }

}
