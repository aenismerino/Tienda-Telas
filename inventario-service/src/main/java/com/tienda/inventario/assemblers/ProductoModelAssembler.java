package com.tienda.inventario.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.controller.ProductoControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoDTO, EntityModel<ProductoDTO>> {

    @Override
    public EntityModel<ProductoDTO> toModel(ProductoDTO productoDTO) {
        return EntityModel.of(productoDTO,
                linkTo(methodOn(ProductoControllerV2.class).obtenerProductoV2(productoDTO.getId())).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).listarProductosV2()).withRel("productos"));
    }
}