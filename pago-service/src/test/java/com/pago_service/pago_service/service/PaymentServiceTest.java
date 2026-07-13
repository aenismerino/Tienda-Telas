package com.pago_service.pago_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.pago_service.pago_service.DTO.PaymentDTO;
import com.pago_service.pago_service.model.OrderClient;
import com.pago_service.pago_service.model.Payment;
import com.pago_service.pago_service.repository.PaymentsRepository;
import com.pago_service.pago_service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentsRepository paymentsRepository;

    @Mock
    private OrderClient orderClient;

    private Payment payment;
    private PaymentDTO paymentDTO;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(100L);
        payment.setMonto(25000);
        payment.setEstado("APROBADO");
        payment.setFechaPago(LocalDateTime.now());

        paymentDTO = PaymentDTO.builder()
                .id(1L)
                .orderId(100L)
                .monto(25000)
                .build();
    }

    @Test
    void testObtenerTodos() {
        when(paymentsRepository.findAll()).thenReturn(List.of(payment));
        List<Payment> pagos = paymentService.obtenerTodos();
        assertNotNull(pagos);
        assertEquals(1, pagos.size());
        verify(paymentsRepository).findAll();
    }

    @Test
    void testProcesarPago() {
        when(paymentsRepository.save(any(Payment.class))).thenReturn(payment);
        doNothing().when(orderClient).actualizarEstadoPedido(100L, "PAGADO");

        Payment resultado = paymentService.procesarPago(paymentDTO);
        
        assertNotNull(resultado);
        assertEquals("APROBADO", resultado.getEstado());
        verify(paymentsRepository).save(any(Payment.class));
        verify(orderClient).actualizarEstadoPedido(100L, "PAGADO");
    }

    @Test
    void testObtenerPorId() {
        when(paymentsRepository.findById(1L)).thenReturn(Optional.of(payment));
        Optional<Payment> resultado = paymentService.obtenerPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals(100L, resultado.get().getOrderId());
    }

    @Test
    void testActualizar() {
        when(paymentsRepository.findById(1L)).thenReturn(Optional.of(payment));
        
        Payment actualizado = new Payment();
        actualizado.setId(1L);
        actualizado.setOrderId(100L);
        actualizado.setMonto(30000);
        
        when(paymentsRepository.save(any(Payment.class))).thenReturn(actualizado);

        PaymentDTO dtoUpdate = PaymentDTO.builder()
                .orderId(100L)
                .monto(30000)
                .build();

        Payment resultado = paymentService.actualizar(1L, dtoUpdate);
        assertNotNull(resultado);
        assertEquals(30000, resultado.getMonto());
    }

    @Test
    void testEliminar() {
        when(paymentsRepository.existsById(1L)).thenReturn(true);
        doNothing().when(paymentsRepository).deleteById(1L);

        assertDoesNotThrow(() -> paymentService.eliminar(1L));
        verify(paymentsRepository, times(1)).deleteById(1L);
    }
}
