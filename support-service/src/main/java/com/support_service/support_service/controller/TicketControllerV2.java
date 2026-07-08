package com.support_service.support_service.controller;

import com.support_service.support_service.DTO.TicketDTO;
import com.support_service.support_service.assemblers.TicketModelAssembler;
import com.support_service.support_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets/v2")
@RequiredArgsConstructor
public class TicketControllerV2 {

    private final TicketService ticketService;
    private final TicketModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<TicketDTO>>> listarTicketsV2() {
        List<EntityModel<TicketDTO>> tickets = ticketService.obtenerTodos().stream()
                .map(TicketDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<TicketDTO>> collectionModel = CollectionModel.of(tickets)
                .add(linkTo(methodOn(TicketControllerV2.class).listarTicketsV2()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TicketDTO>> obtenerTicketV2(@PathVariable Long id) {
        return ticketService.obtenerPorId(id)
                .map(TicketDTO::fromModel)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
