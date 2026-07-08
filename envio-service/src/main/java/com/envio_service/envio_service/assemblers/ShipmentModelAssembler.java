package com.envio_service.envio_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.envio_service.envio_service.DTO.ShipmentDTO;
import com.envio_service.envio_service.controller.ShipmentControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ShipmentModelAssembler implements RepresentationModelAssembler<ShipmentDTO, EntityModel<ShipmentDTO>> {

    @Override
    public EntityModel<ShipmentDTO> toModel(ShipmentDTO shipmentDTO) {
        return EntityModel.of(shipmentDTO,
                linkTo(methodOn(ShipmentControllerV2.class).obtenerEnvioV2(shipmentDTO.getId())).withSelfRel(),
                linkTo(methodOn(ShipmentControllerV2.class).listarEnviosV2()).withRel("shipments"));
    }
}
