package com.support_service.support_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import com.support_service.support_service.model.Ticket;
import com.support_service.support_service.repository.TicketRepository;

import java.util.List;

@RestController
@RequestMapping("/api/support")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        if (ticket.getFecha() == null) {
            ticket.setFecha(LocalDateTime.now());
        }
        if (ticket.getEstado() == null) {
            ticket.setEstado("ABIERTO");
        }
        return ticketRepository.save(ticket);
    }
}
