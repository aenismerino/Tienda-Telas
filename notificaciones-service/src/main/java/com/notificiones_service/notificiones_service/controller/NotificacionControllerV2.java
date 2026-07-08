package com.notificiones_service.notificiones_service.controller;

import com.notificiones_service.notificiones_service.DTO.NotificacionDTO;
import com.notificiones_service.notificiones_service.assemblers.NotificacionModelAssembler;
import com.notificiones_service.notificiones_service.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones/v2")
@RequiredArgsConstructor
public class NotificacionControllerV2 {

    private final NotificacionService notificacionService;
    private final NotificacionModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<NotificacionDTO>>> listarNotificacionesV2() {
        List<EntityModel<NotificacionDTO>> notificaciones = notificacionService.obtenerTodas().stream()
                .map(NotificacionDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<NotificacionDTO>> collectionModel = CollectionModel.of(notificaciones)
                .add(linkTo(methodOn(NotificacionControllerV2.class).listarNotificacionesV2()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<NotificacionDTO>> obtenerNotificacionV2(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(NotificacionDTO::fromModel)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
