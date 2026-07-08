package com.notificiones_service.notificiones_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.notificiones_service.notificiones_service.DTO.NotificacionDTO;
import com.notificiones_service.notificiones_service.model.Notificacion;
import com.notificiones_service.notificiones_service.service.NotificacionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> getAllNotificaciones() {
        List<NotificacionDTO> lista = notificacionService.obtenerTodas().stream()
                .map(NotificacionDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDTO> getNotificacionById(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(NotificacionDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NotificacionDTO> createNotificacion(@Valid @RequestBody NotificacionDTO dto) {
        Notificacion creada = notificacionService.crearNotificacion(dto);
        return new ResponseEntity<>(NotificacionDTO.fromModel(creada), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
