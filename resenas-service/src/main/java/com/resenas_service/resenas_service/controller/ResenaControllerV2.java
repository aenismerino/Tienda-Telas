package com.resenas_service.resenas_service.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resenas_service.resenas_service.assemblers.ResenaModelAssembler;
import com.resenas_service.resenas_service.DTO.ResenaDto;
import com.resenas_service.resenas_service.model.Resena;
import com.resenas_service.resenas_service.service.ResenasService;

@RestController
@RequestMapping("/v2/resenas")
public class ResenaControllerV2 {

    private final ResenasService resenaService;
    private final ResenaModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(ResenaControllerV2.class);

    public ResenaControllerV2(ResenasService resenaService, ResenaModelAssembler assembler) {
        this.resenaService = resenaService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Resena>> listarResenas() {
        logger.info("V2 GET /resenas - Listando resenas");
        List<EntityModel<Resena>> resenas = resenaService.listarTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(resenas, linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Resena> obtenerResena(@PathVariable Long id) {
        logger.info("V2 GET /resenas/{} - Obteniendo resena", id);
        Resena resena = resenaService.obtenerPorId(id);
        return assembler.toModel(resena);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Resena>> crearResena(@Valid @RequestBody ResenaDto dto) {
        logger.info("V2 POST /resenas - Creando resena");
        Resena nuevaResena = resenaService.crearResena(dto);
        EntityModel<Resena> resenaModel = assembler.toModel(nuevaResena);

        return ResponseEntity.created(resenaModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(resenaModel);

    }
}