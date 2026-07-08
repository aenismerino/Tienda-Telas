package com.pago_service.pago_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.pago_service.pago_service.DTO.PaymentDTO;
import com.pago_service.pago_service.controller.PaymentControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PaymentModelAssembler implements RepresentationModelAssembler<PaymentDTO, EntityModel<PaymentDTO>> {

    @Override
    public EntityModel<PaymentDTO> toModel(PaymentDTO paymentDTO) {
        return EntityModel.of(paymentDTO,
                linkTo(methodOn(PaymentControllerV2.class).obtenerPagoV2(paymentDTO.getId())).withSelfRel(),
                linkTo(methodOn(PaymentControllerV2.class).listarPagosV2()).withRel("payments"));
    }
}
