package com.pago_service.pago_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pago_service.pago_service.DTO.PaymentDTO;
import com.pago_service.pago_service.model.Payment;
import com.pago_service.pago_service.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private ObjectMapper objectMapper;

    private Payment payment;
    private PaymentDTO paymentDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(100L);
        payment.setMonto(25000);
        payment.setEstado("APROBADO");
        payment.setFechaPago(LocalDateTime.now());

        paymentDTO = PaymentDTO.fromModel(payment);

        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testListarPagos() throws Exception {
        when(paymentService.obtenerTodos()).thenReturn(List.of(payment));

        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].orderId").value(100));
    }

    @Test
    public void testRegistrarPago() throws Exception {
        when(paymentService.procesarPago(any(PaymentDTO.class))).thenReturn(payment);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("APROBADO"));
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(paymentService.obtenerPorId(1L)).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderId").value(100));
    }

    @Test
    public void testEliminarPago() throws Exception {
        doNothing().when(paymentService).eliminar(1L);

        mockMvc.perform(delete("/payments/1"))
                .andExpect(status().isNoContent());

        verify(paymentService, times(1)).eliminar(1L);
    }
}
