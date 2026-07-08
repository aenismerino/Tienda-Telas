package com.notificiones_service.notificiones_service.service;

import com.notificiones_service.notificiones_service.DTO.NotificacionDTO;
import com.notificiones_service.notificiones_service.model.Notificacion;
import com.notificiones_service.notificiones_service.repository.NotificacionRepository;
import com.notificiones_service.notificiones_service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> obtenerTodas() {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> obtenerPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    public Notificacion crearNotificacion(NotificacionDTO dto) {
        Notificacion notificacion = dto.toModel();
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setEstado("CREADA");
        return notificacionRepository.save(notificacion);
    }

    public void eliminarNotificacion(Long id) {
        if (!notificacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notificación no encontrada con ID: " + id);
        }
        notificacionRepository.deleteById(id);
    }
}
