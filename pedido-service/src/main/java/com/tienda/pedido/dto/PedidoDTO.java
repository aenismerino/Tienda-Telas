package com.tienda.pedido.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PedidoDTO {
    private Integer id;
    private Integer productoId;
    private Integer cantidad;
    private Integer precioUnitario;
    private Integer totalPedido;
    private LocalDateTime fechaPedido;
}
