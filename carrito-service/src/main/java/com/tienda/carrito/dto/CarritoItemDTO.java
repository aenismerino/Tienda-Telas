package com.tienda.carrito.dto;

import lombok.Data;

@Data
public class CarritoItemDTO {
    private Integer id;
    private String usuarioId;
    private Integer productoId;
    private Integer cantidad;
    private Integer precioUnitario;
    private Boolean seleccionado;
}
