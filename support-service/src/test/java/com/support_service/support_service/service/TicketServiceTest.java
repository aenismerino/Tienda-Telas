package com.support_service.support_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.support_service.support_service.DTO.TicketDTO;
import com.support_service.support_service.model.Ticket;
import com.support_service.support_service.repository.TicketRepository;
import com.support_service.support_service.exception.ResourceNotFoundException;
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
class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    private Ticket ticket;
    private TicketDTO ticketDTO;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setAsunto("Ayuda");
        ticket.setDescripcion("No puedo comprar");
        ticket.setEstado("ABIERTO");
        ticket.setFecha(LocalDateTime.now());

        ticketDTO = TicketDTO.builder()
                .id(1L)
                .asunto("Ayuda")
                .descripcion("No puedo comprar")
                .build();
    }

    @Test
    void testObtenerTodos() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));
        List<Ticket> result = ticketService.obtenerTodos();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testObtenerPorId() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        Optional<Ticket> result = ticketService.obtenerPorId(1L);
        assertTrue(result.isPresent());
        assertEquals("Ayuda", result.get().getAsunto());
    }

    @Test
    void testCrearTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket resultado = ticketService.crearTicket(ticketDTO);
        
        assertNotNull(resultado);
        assertEquals("ABIERTO", resultado.getEstado());
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testEliminarTicket() {
        when(ticketRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ticketRepository).deleteById(1L);

        assertDoesNotThrow(() -> ticketService.eliminarTicket(1L));
        verify(ticketRepository, times(1)).deleteById(1L);
    }
}
