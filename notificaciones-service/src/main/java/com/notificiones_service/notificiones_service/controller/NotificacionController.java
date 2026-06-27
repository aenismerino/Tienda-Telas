package com.notificiones_service.notificiones_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import com.notificiones_service.notificiones_service.model.Notificacion;
import com.notificiones_service.notificiones_service.repository.NotificacionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @GetMapping
    public List<Notificacion> getAllNotificaciones() {
        return notificacionRepository.findAll();
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
}
