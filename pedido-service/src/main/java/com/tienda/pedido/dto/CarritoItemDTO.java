package com.tienda.pedido.dto;

import lombok.Data;

@Data
public class CarritoItemDTO {
    private Integer id;
    private Integer usuarioId;
    private Integer productoId;
    private Integer cantidad;
    private Integer precioUnitario;
    private Boolean seleccionado;
}
