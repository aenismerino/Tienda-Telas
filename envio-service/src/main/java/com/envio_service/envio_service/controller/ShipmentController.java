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

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.stream.Collectors;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Shipment>>> listarEnvios() {
        log.info("Consultando la lista completa de envios");
        List<EntityModel<Shipment>> envios = shipmentService.obtenerTodos().stream()
                .map(envio -> EntityModel.of(envio,
                        linkTo(methodOn(ShipmentController.class).obtenerEnvio(envio.getId())).withSelfRel(),
                        linkTo(methodOn(ShipmentController.class).listarEnvios()).withRel("envios")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(envios,
                linkTo(methodOn(ShipmentController.class).listarEnvios()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Shipment>> obtenerEnvio(@PathVariable Long id) {
        log.info("Buscando detalles del envio: {}", id);
        return shipmentService.obtenerPorId(id)
                .map(envio -> {
                    EntityModel<Shipment> resource = EntityModel.of(envio,
                            linkTo(methodOn(ShipmentController.class).obtenerEnvio(id)).withSelfRel(),
                            linkTo(methodOn(ShipmentController.class).listarEnvios()).withRel("envios"));
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Shipment> crearEnvio(@RequestBody ShipmentDTO shipmentDTO) {
        log.info("Registrando nuevo envio para la orden: {}", shipmentDTO.getOrderId());
        Shipment nuevoEnvio = shipmentService.crearEnvio(shipmentDTO);
        log.info("Envio creado exitosamente: {}", nuevoEnvio.getId());
        return new ResponseEntity<>(nuevoEnvio, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/tracking")
    public ResponseEntity<Shipment> actualizarTracking(@PathVariable Long id, @RequestBody TrackingDTO trackingDTO) {
        log.info("Actualizando estado de envio : {} a -> {}", id, trackingDTO.getNuevaDescripcion());
        try {
            Shipment envioActualizado = shipmentService.agregarHistorial(id, trackingDTO);
            return ResponseEntity.ok(envioActualizado);
        } catch ( Exception e ) {
            log.error("Error al actualizar el envio: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long id) {
        log.info("Eliminando envio: {}", id);
        try {
            shipmentService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/totales-estado")
    public ResponseEntity<java.util.Map<String, Long>> totalesPorEstado() {
        log.info("Consultando totales por estado");
        return ResponseEntity.ok(shipmentService.totalesPorEstado());
    }

    @GetMapping("/buscar-rut/{rut}")
    public ResponseEntity<CollectionModel<EntityModel<Shipment>>> buscarPorRut(@PathVariable String rut) {
        log.info("Buscando envios para el RUT: {}", rut);
        List<EntityModel<Shipment>> envios = shipmentService.buscarPorRut(rut).stream()
                .map(envio -> EntityModel.of(envio,
                        linkTo(methodOn(ShipmentController.class).obtenerEnvio(envio.getId())).withSelfRel(),
                        linkTo(methodOn(ShipmentController.class).buscarPorRut(rut)).withRel("busqueda-rut")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(envios,
                linkTo(methodOn(ShipmentController.class).buscarPorRut(rut)).withSelfRel()));
    }


}
