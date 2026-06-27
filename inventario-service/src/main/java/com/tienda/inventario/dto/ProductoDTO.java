package com.tienda.inventario.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Integer id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private Integer precio;
    private Integer stock;
    private Double metros;
}
