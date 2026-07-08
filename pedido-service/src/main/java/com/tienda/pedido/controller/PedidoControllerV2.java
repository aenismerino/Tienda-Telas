package com.tienda.pedido.controller;

import com.tienda.pedido.dto.PedidoDTO;
import com.tienda.pedido.assemblers.PedidoModelAssembler;
import com.tienda.pedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/pedidos/v2")
@RequiredArgsConstructor
public class PedidoControllerV2 {

    private final PedidoService pedidoService;
    private final PedidoModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PedidoDTO>>> listarPedidosV2() {
        log.info("V2 GET /pedidos/v2 - Listando pedidos con HATEOAS");
        List<EntityModel<PedidoDTO>> pedidos = pedidoService.listarTodo().stream()
                .map(PedidoDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PedidoDTO>> collectionModel = CollectionModel.of(pedidos)
                .add(linkTo(methodOn(PedidoControllerV2.class).listarPedidosV2()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoDTO>> obtenerPedidoV2(@PathVariable Integer id) {
        log.info("V2 GET /pedidos/v2/{} - Buscando pedido con HATEOAS", id);
        return pedidoService.buscarPorId(id)
                .map(PedidoDTO::fromModel)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
