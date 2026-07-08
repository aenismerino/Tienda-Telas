package com.tienda.pedido.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tienda.pedido.dto.PedidoDTO;
import com.tienda.pedido.service.PedidoService;
import com.tienda.pedido.model.Pedido;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listar() {
        log.info("Listando todos los pedidos");
        List<PedidoDTO> pedidos = pedidoService.listarTodo().stream()
                .map(PedidoDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPorId(@PathVariable Integer id) {
        log.info("Buscando pedido con ID: {}", id);
        return pedidoService.buscarPorId(id)
                .map(PedidoDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<PedidoDTO> crear(@Valid @RequestBody PedidoDTO dto) {
        log.info("Creando pedido de producto ID: {}", dto.getProductoId());
        Pedido creado = pedidoService.guardar(dto);
        return new ResponseEntity<>(PedidoDTO.fromModel(creado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody PedidoDTO dto) {
        log.info("Actualizando pedido con ID: {}", id);
        Pedido actualizado = pedidoService.actualizar(id, dto);
        return ResponseEntity.ok(PedidoDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("Eliminando pedido con ID: {}", id);
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstadoPedido(@PathVariable Integer id,
            @RequestParam("estado") String estado) {
        log.info("Actualizando estado de pedido {} a {}", id, estado);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/totales")
    public ResponseEntity<Integer> obtenerTotales() {
        return ResponseEntity.ok(pedidoService.obtenerTotalVentas());
    }

    @GetMapping("/buscar-fecha")
    public ResponseEntity<List<PedidoDTO>> buscarPorFecha(
            @RequestParam("inicio") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime inicio,
            @RequestParam("fin") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime fin) {

        List<PedidoDTO> pedidos = pedidoService.buscarPorFecha(inicio, fin).stream()
                .map(PedidoDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }
}
