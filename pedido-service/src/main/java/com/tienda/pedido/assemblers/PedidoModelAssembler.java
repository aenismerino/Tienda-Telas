package com.tienda.pedido.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.tienda.pedido.dto.PedidoDTO;
import com.tienda.pedido.controller.PedidoControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<PedidoDTO, EntityModel<PedidoDTO>> {
    
    @Override
    public EntityModel<PedidoDTO> toModel(PedidoDTO pedidoDTO) {
        return EntityModel.of(pedidoDTO,
                linkTo(methodOn(PedidoControllerV2.class).obtenerPedidoV2(pedidoDTO.getId())).withSelfRel(),
                linkTo(methodOn(PedidoControllerV2.class).listarPedidosV2()).withRel("pedidos"));
    }
}