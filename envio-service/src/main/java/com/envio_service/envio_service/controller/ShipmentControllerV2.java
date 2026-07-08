package com.envio_service.envio_service.controller;

import com.envio_service.envio_service.DTO.ShipmentDTO;
import com.envio_service.envio_service.assemblers.ShipmentModelAssembler;
import com.envio_service.envio_service.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/shipments/v2")
@RequiredArgsConstructor
public class ShipmentControllerV2 {

    private final ShipmentService shipmentService;
    private final ShipmentModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ShipmentDTO>>> listarEnviosV2() {
        log.info("V2 GET /shipments/v2 - Listando envíos con HATEOAS");
        List<EntityModel<ShipmentDTO>> shipments = shipmentService.obtenerTodos().stream()
                .map(ShipmentDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ShipmentDTO>> collectionModel = CollectionModel.of(shipments)
                .add(linkTo(methodOn(ShipmentControllerV2.class).listarEnviosV2()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ShipmentDTO>> obtenerEnvioV2(@PathVariable Long id) {
        log.info("V2 GET /shipments/v2/{} - Buscando envío con HATEOAS", id);
        return shipmentService.obtenerPorId(id)
                .map(ShipmentDTO::fromModel)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
