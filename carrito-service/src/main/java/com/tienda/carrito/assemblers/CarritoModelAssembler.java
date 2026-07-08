package com.tienda.carrito.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.tienda.carrito.dto.CarritoResponseDTO;
import com.tienda.carrito.controller.CarritoControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CarritoModelAssembler implements RepresentationModelAssembler<CarritoResponseDTO, EntityModel<CarritoResponseDTO>> {

    @Override
    public EntityModel<CarritoResponseDTO> toModel(CarritoResponseDTO dto) {
        // Asumiendo que el dto tiene los items y buscamos por usuarioId que vendrá de la sesión o del primer item
        String usuarioId = dto.getItems().isEmpty() ? "unknown" : dto.getItems().get(0).getUsuarioId();
        
        return EntityModel.of(dto,
                linkTo(methodOn(CarritoControllerV2.class).obtenerCarritoV2(usuarioId)).withSelfRel());
    }
}
