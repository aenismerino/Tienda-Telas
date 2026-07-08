package com.catalogo_service.catalogo_service.DTO;

import lombok.Data;

@Data
public class ProductoDto {
    private Long id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private Integer precio;
    private Integer stock;
}
