package com.support_service.support_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.support_service.support_service.DTO.TicketDTO;
import com.support_service.support_service.controller.TicketControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TicketModelAssembler implements RepresentationModelAssembler<TicketDTO, EntityModel<TicketDTO>> {

    @Override
    public EntityModel<TicketDTO> toModel(TicketDTO ticketDTO) {
        return EntityModel.of(ticketDTO,
                linkTo(methodOn(TicketControllerV2.class).obtenerTicketV2(ticketDTO.getId())).withSelfRel(),
                linkTo(methodOn(TicketControllerV2.class).listarTicketsV2()).withRel("tickets"));
    }
}
