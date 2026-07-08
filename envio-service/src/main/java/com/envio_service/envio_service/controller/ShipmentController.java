package com.envio_service.envio_service.controller;

import com.envio_service.envio_service.model.Shipment;
import com.envio_service.envio_service.DTO.ShipmentDTO;
import com.envio_service.envio_service.DTO.TrackingDTO;
import com.envio_service.envio_service.service.ShipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping
    public ResponseEntity<List<ShipmentDTO>> listarEnvios() {
        log.info("Consultando la lista completa de envios (V1)");
        List<ShipmentDTO> envios = shipmentService.obtenerTodos().stream()
                .map(ShipmentDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(envios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDTO> obtenerEnvio(@PathVariable Long id) {
        log.info("Buscando detalles del envio: {}", id);
        return shipmentService.obtenerPorId(id)
                .map(ShipmentDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShipmentDTO> crearEnvio(@Valid @RequestBody ShipmentDTO shipmentDTO) {
        log.info("Registrando nuevo envio para la orden: {}", shipmentDTO.getOrderId());
        Shipment nuevoEnvio = shipmentService.crearEnvio(shipmentDTO);
        log.info("Envio creado exitosamente: {}", nuevoEnvio.getId());
        return new ResponseEntity<>(ShipmentDTO.fromModel(nuevoEnvio), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/tracking")
    public ResponseEntity<ShipmentDTO> actualizarTracking(@PathVariable Long id,
            @Valid @RequestBody TrackingDTO trackingDTO) {
        log.info("Actualizando estado de envio : {} a -> {}", id, trackingDTO.getNuevaDescripcion());
        Shipment envioActualizado = shipmentService.agregarHistorial(id, trackingDTO);
        return ResponseEntity.ok(ShipmentDTO.fromModel(envioActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long id) {
        log.info("Eliminando envio: {}", id);
        shipmentService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/totales-estado")
    public ResponseEntity<java.util.Map<String, Long>> totalesPorEstado() {
        log.info("Consultando totales por estado");
        return ResponseEntity.ok(shipmentService.totalesPorEstado());
    }

    @GetMapping("/buscar-rut/{rut}")
    public ResponseEntity<List<ShipmentDTO>> buscarPorRut(@PathVariable String rut) {
        log.info("Buscando envios para el RUT: {}", rut);
        List<ShipmentDTO> envios = shipmentService.buscarPorRut(rut).stream()
                .map(ShipmentDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(envios);
    }
}
