package com.notificiones_service.notificiones_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.notificiones_service.notificiones_service.DTO.NotificacionDTO;
import com.notificiones_service.notificiones_service.controller.NotificacionControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class NotificacionModelAssembler implements RepresentationModelAssembler<NotificacionDTO, EntityModel<NotificacionDTO>> {

    @Override
    public EntityModel<NotificacionDTO> toModel(NotificacionDTO notificacionDTO) {
        return EntityModel.of(notificacionDTO,
                linkTo(methodOn(NotificacionControllerV2.class).obtenerNotificacionV2(notificacionDTO.getId())).withSelfRel(),
                linkTo(methodOn(NotificacionControllerV2.class).listarNotificacionesV2()).withRel("notificaciones"));
    }
}
