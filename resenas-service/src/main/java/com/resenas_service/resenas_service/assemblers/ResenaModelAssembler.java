package com.resenas_service.resenas_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.resenas_service.resenas_service.controller.ResenaController;
import com.resenas_service.resenas_service.controller.ResenaControllerV2;
import com.resenas_service.resenas_service.model.Resena;
import com.resenas_service.resenas_service.service.ResenasService;

@Component
public class ResenaModelAssembler implements RepresentationModelAssembler<Resena, EntityModel<Resena>> {

    @Override
    public EntityModel<Resena> toModel(Resena resena) {
        return EntityModel.of(resena,
                linkTo(methodOn(ResenaControllerV2.class).obtenerResena(resena.getId())).withSelfRel(),
                linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withRel("resenas"));
    }
}