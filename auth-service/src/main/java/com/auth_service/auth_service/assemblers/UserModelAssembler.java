package com.auth_service.auth_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.auth_service.auth_service.DTO.UserDTO;
import com.auth_service.auth_service.controller.AuthControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    @Override
    public EntityModel<UserDTO> toModel(UserDTO userDTO) {
        return EntityModel.of(userDTO,
                linkTo(methodOn(AuthControllerV2.class).obtenerUsuarioPorRutV2(userDTO.getRut())).withSelfRel(),
                linkTo(methodOn(AuthControllerV2.class).listarUsuariosV2()).withRel("usuarios"));
    }
}