package com.notificiones_service.notificiones_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import com.notificiones_service.notificiones_service.model.Notificacion;
import com.notificiones_service.notificiones_service.repository.NotificacionRepository;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @GetMapping
    public CollectionModel<EntityModel<Notificacion>> getAllNotificaciones() {
        List<EntityModel<Notificacion>> notificaciones = notificacionRepository.findAll().stream()
                .map(notificacion -> EntityModel.of(notificacion,
                        linkTo(methodOn(NotificacionController.class).getNotificacionById(notificacion.getId())).withSelfRel(),
                        linkTo(methodOn(NotificacionController.class).getAllNotificaciones()).withRel("notificaciones")))
                .collect(Collectors.toList());

        return CollectionModel.of(notificaciones,
                linkTo(methodOn(NotificacionController.class).getAllNotificaciones()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Notificacion>> getNotificacionById(@PathVariable Long id) {
        return notificacionRepository.findById(id)
                .map(notificacion -> {
                    EntityModel<Notificacion> resource = EntityModel.of(notificacion,
                            linkTo(methodOn(NotificacionController.class).getNotificacionById(id)).withSelfRel(),
                            linkTo(methodOn(NotificacionController.class).getAllNotificaciones()).withRel("notificaciones"));
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Notificacion createNotificacion(@RequestBody Notificacion notificacion) {
        if (notificacion.getFecha() == null) {
            notificacion.setFecha(LocalDateTime.now());
        }
        if (notificacion.getEstado() == null) {
            notificacion.setEstado("CREADA");
        }
        return notificacionRepository.save(notificacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> updateNotificacion(@PathVariable Long id, @RequestBody Notificacion notificacionDetails) {
        return notificacionRepository.findById(id)
                .map(notificacion -> {
                    notificacion.setMensaje(notificacionDetails.getMensaje());
                    notificacion.setDestinatario(notificacionDetails.getDestinatario());
                    notificacion.setEstado(notificacionDetails.getEstado());
                    Notificacion updated = notificacionRepository.save(notificacion);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        if (notificacionRepository.existsById(id)) {
            notificacionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
