package com.support_service.support_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import com.support_service.support_service.model.Ticket;
import com.support_service.support_service.repository.TicketRepository;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/support")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public CollectionModel<EntityModel<Ticket>> getAllTickets() {
        List<EntityModel<Ticket>> tickets = ticketRepository.findAll().stream()
                .map(ticket -> EntityModel.of(ticket,
                        linkTo(methodOn(TicketController.class).getTicketById(ticket.getId())).withSelfRel(),
                        linkTo(methodOn(TicketController.class).getAllTickets()).withRel("tickets")))
                .collect(Collectors.toList());

        return CollectionModel.of(tickets,
                linkTo(methodOn(TicketController.class).getAllTickets()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Ticket>> getTicketById(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    EntityModel<Ticket> resource = EntityModel.of(ticket,
                            linkTo(methodOn(TicketController.class).getTicketById(id)).withSelfRel(),
                            linkTo(methodOn(TicketController.class).getAllTickets()).withRel("tickets"));
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
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

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticketDetails) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setAsunto(ticketDetails.getAsunto());
                    ticket.setDescripcion(ticketDetails.getDescripcion());
                    ticket.setEstado(ticketDetails.getEstado());
                    Ticket updated = ticketRepository.save(ticket);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
