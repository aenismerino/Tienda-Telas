package com.pago_service.pago_service.controller;

import com.pago_service.pago_service.DTO.PaymentDTO;
import com.pago_service.pago_service.model.Payment;
import com.pago_service.pago_service.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> listarPagos() {
        log.info("Accediendo al historial de pagos");
        return ResponseEntity.ok(paymentService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<Payment> registrarPago(@RequestBody PaymentDTO paymentDTO) {
        log.info("Procesando pago para la orden: {}", paymentDTO.getOrderId());
        Payment nuevoPago = paymentService.procesarPago(paymentDTO);
        log.info("Pago: {} aprobado exitosamente", nuevoPago.getId());
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> obtenerPorId(@PathVariable Long id) {
        log.info("Buscando pago con ID: {}", id);
        return paymentService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> actualizarPago(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        log.info("Actualizando pago con ID: {}", id);
        try {
            return ResponseEntity.ok(paymentService.actualizar(id, paymentDTO));
        } catch (RuntimeException e) {
            log.error("Error al actualizar pago: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        log.info("Eliminando pago con ID: {}", id);
        try {
            paymentService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error al eliminar pago: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
