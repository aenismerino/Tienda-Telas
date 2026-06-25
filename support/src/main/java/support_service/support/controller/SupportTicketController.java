package support_service.support.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import support_service.support.DTO.SupportTicketDTO;
import support_service.support.model.SupportTicket;
import support_service.support.service.SupportTicketService;
import support_service.support.assembler.SupportTicketModelAssembler; // <-- Importa el nuevo assembler

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Support Ticket Controller", description = "Endpoints para la gestión de tickets de soporte")
public class SupportTicketController {

    private static final Logger log = LoggerFactory.getLogger(SupportTicketController.class);
    private final SupportTicketService ticketService;
    private final SupportTicketModelAssembler assembler; // <-- Declara el assembler

    // Inyectamos ambos componentes en el constructor
    public SupportTicketController(SupportTicketService ticketService, SupportTicketModelAssembler assembler) {
        this.ticketService = ticketService;
        this.assembler = assembler;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo ticket")
    public ResponseEntity<SupportTicket> createTicket(@Valid @RequestBody SupportTicketDTO ticketDTO) {
        log.info("Petición REST para crear un ticket");
        SupportTicket createdTicket = ticketService.createTicket(ticketDTO);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ticket por ID")
    public EntityModel<SupportTicket> getTicketById(@PathVariable Long id) {
        log.info("Petición REST para obtener el ticket con ID: {}", id);
        SupportTicket ticket = ticketService.getTicketById(id);
        
        // El assembler se encarga de empaquetar el objeto con sus links automáticamente
        return assembler.toModel(ticket); 
    }

    @GetMapping
    @Operation(summary = "Obtener todos los tickets")
    public CollectionModel<EntityModel<SupportTicket>> getAllTickets() {
        log.info("Petición REST para listar todos los tickets");
        List<SupportTicket> tickets = ticketService.getAllTickets();

        // Transformamos la lista usando la lógica centralizada del assembler
        List<EntityModel<SupportTicket>> ticketModels = tickets.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<SupportTicket>> collectionModel = CollectionModel.of(ticketModels);
        collectionModel.add(linkTo(methodOn(SupportTicketController.class).getAllTickets()).withSelfRel());

        return collectionModel;
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar el estado de un ticket")
    public ResponseEntity<SupportTicket> updateTicketStatus(@PathVariable Long id, @RequestParam String status) {
        log.info("Petición REST para actualizar estado del ticket");
        SupportTicket updatedTicket = ticketService.updateTicketStatus(id, status);
        return ResponseEntity.ok(updatedTicket);
    }
}
