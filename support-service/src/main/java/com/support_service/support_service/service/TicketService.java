package com.support_service.support_service.service;

import com.support_service.support_service.DTO.TicketDTO;
import com.support_service.support_service.model.Ticket;
import com.support_service.support_service.repository.TicketRepository;
import com.support_service.support_service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> obtenerTodos() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> obtenerPorId(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket crearTicket(TicketDTO dto) {
        Ticket ticket = dto.toModel();
        ticket.setFecha(LocalDateTime.now());
        ticket.setEstado("ABIERTO");
        return ticketRepository.save(ticket);
    }

    public void eliminarTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket no encontrado con ID: " + id);
        }
        ticketRepository.deleteById(id);
    }
}
