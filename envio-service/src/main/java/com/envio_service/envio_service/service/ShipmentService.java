package com.envio_service.envio_service.service;

import com.envio_service.envio_service.DTO.*;
import com.envio_service.envio_service.model.*;
import com.envio_service.envio_service.repository.ShipmentRepository;
import com.envio_service.envio_service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public List<Shipment> obtenerTodos() {
        return shipmentRepository.findAll();
    }

    public Optional<Shipment> obtenerPorId(Long id) {
        return shipmentRepository.findById(id);
    }

    public Shipment crearEnvio(ShipmentDTO dto) {
        Shipment envio = dto.toModel();
        envio.setEstadoActual("PREPARANDO PAQUETE");

        TrackingHistory primerPaso = new TrackingHistory();
        primerPaso.setDescripcion("PREPARANDO PAQUETE");
        primerPaso.setFechaEvento(LocalDateTime.now());
        primerPaso.setShipment(envio);

        envio.getHistorial().add(primerPaso);

        return shipmentRepository.save(envio);
    }

    public Shipment agregarHistorial(Long shipmentId, TrackingDTO trackingDTO) {
        Shipment envio = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado con ID: " + shipmentId));

        envio.setEstadoActual(trackingDTO.getNuevaDescripcion());

        TrackingHistory nuevoPaso = new TrackingHistory();
        nuevoPaso.setDescripcion(trackingDTO.getNuevaDescripcion());
        nuevoPaso.setFechaEvento(LocalDateTime.now());
        nuevoPaso.setShipment(envio);

        envio.getHistorial().add(nuevoPaso);

        return shipmentRepository.save(envio);
    }

    public void eliminar(Long id) {
        if (!shipmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Envío no encontrado con ID: " + id);
        }
        shipmentRepository.deleteById(id);
    }

    public java.util.Map<String, Long> totalesPorEstado() {
        return shipmentRepository.findAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Shipment::getEstadoActual,
                        java.util.stream.Collectors.counting()
                ));
    }

    public List<Shipment> buscarPorRut(String rut) {
        return shipmentRepository.findAll().stream()
                .filter(s -> s.getDireccionDestino() != null)
                .collect(java.util.stream.Collectors.toList());
    }
}
