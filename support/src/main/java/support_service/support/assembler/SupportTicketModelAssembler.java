package support_service.support.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import support_service.support.controller.SupportTicketController;
import support_service.support.model.SupportTicket;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SupportTicketModelAssembler implements RepresentationModelAssembler<SupportTicket, EntityModel<SupportTicket>> {

    @Override
    public EntityModel<SupportTicket> toModel(SupportTicket ticket) {
        return EntityModel.of(ticket,
            linkTo(methodOn(SupportTicketController.class).getTicketById(ticket.getId())).withSelfRel(),
            linkTo(methodOn(SupportTicketController.class).getAllTickets()).withRel("all-tickets"));
    }
}
