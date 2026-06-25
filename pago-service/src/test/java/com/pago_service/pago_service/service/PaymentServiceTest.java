package com.pago_service.pago_service.service;

import com.pago_service.pago_service.DTO.PaymentDTO;
import com.pago_service.pago_service.model.OrderClient;
import com.pago_service.pago_service.model.Payment;
import com.pago_service.pago_service.repository.PaymentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentsRepository paymentsRepository;

    @Mock
    private OrderClient orderClient;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcesarPago() {
        // Given
        PaymentDTO dto = new PaymentDTO();
        dto.setOrderId(1L);
        dto.setMonto(25000);

        Payment savedPayment = new Payment();
        savedPayment.setId(1L);
        savedPayment.setOrderId(1L);
        savedPayment.setMonto(25000);
        savedPayment.setEstado("APROBADO");

        when(paymentsRepository.save(any(Payment.class))).thenReturn(savedPayment);
        doNothing().when(orderClient).actualizarEstadoPedido(1L, "PAGADO");

        // When
        Payment result = paymentService.procesarPago(dto);

        // Then
        assertNotNull(result);
        assertEquals("APROBADO", result.getEstado());
        assertEquals(25000, result.getMonto());
        verify(paymentsRepository, times(1)).save(any(Payment.class));
        verify(orderClient, times(1)).actualizarEstadoPedido(1L, "PAGADO");
    }
}
