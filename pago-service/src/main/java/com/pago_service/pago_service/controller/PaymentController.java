package com.pago_service.pago_service.controller;

import com.pago_service.pago_service.DTO.PaymentDTO;
import com.pago_service.pago_service.model.Payment;
import com.pago_service.pago_service.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> listarPagos() {
        log.info("Accediendo al historial de pagos (V1)");
        List<PaymentDTO> pagos = paymentService.obtenerTodos().stream()
                .map(PaymentDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagos);
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> registrarPago(@Valid @RequestBody PaymentDTO paymentDTO) {
        log.info("Procesando pago para la orden: {}", paymentDTO.getOrderId());
        Payment nuevoPago = paymentService.procesarPago(paymentDTO);
        log.info("Pago: {} aprobado exitosamente", nuevoPago.getId());
        return new ResponseEntity<>(PaymentDTO.fromModel(nuevoPago), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Buscando pago con ID: {}", id);
        return paymentService.obtenerPorId(id)
                .map(PaymentDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> actualizarPago(@PathVariable Long id, @Valid @RequestBody PaymentDTO paymentDTO) {
        log.info("Actualizando pago con ID: {}", id);
        Payment actualizado = paymentService.actualizar(id, paymentDTO);
        return ResponseEntity.ok(PaymentDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        log.info("Eliminando pago con ID: {}", id);
        paymentService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
