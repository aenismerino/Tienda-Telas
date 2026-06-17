package com.pago_service.pago_service.service;

import com.pago_service.pago_service.model.OrderClient;
import com.pago_service.pago_service.DTO.PaymentDTO;
import com.pago_service.pago_service.model.Payment;
import com.pago_service.pago_service.repository.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private OrderClient orderClient;

    public List<Payment> obtenerTodos() {
        return paymentsRepository.findAll();
    }

    public Payment procesarPago(PaymentDTO dto) {
        Payment pago = new Payment();
        pago.setOrderId(dto.getOrderId());
        pago.setMonto(dto.getMonto());
        pago.setEstado("APROBADO");
        pago.setFechaPago(LocalDateTime.now());

        Payment pagoGuardado = paymentsRepository.save(pago);

        try {
            orderClient.actualizarEstadoPedido(pagoGuardado.getOrderId(), "PAGADO");
        } catch ( Exception e ) {
            System.err.println("Error al notificar al order-service: " + e.getMessage());
        }

        return pagoGuardado;
    }

    public java.util.Optional<Payment> obtenerPorId(Long id) {
        return paymentsRepository.findById(id);
    }

    public Payment actualizar(Long id, PaymentDTO dto) {
        Payment pago = paymentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        pago.setOrderId(dto.getOrderId());
        pago.setMonto(dto.getMonto());
        return paymentsRepository.save(pago);
    }

    public void eliminar(Long id) {
        if (!paymentsRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado");
        }
        paymentsRepository.deleteById(id);
    }

}
