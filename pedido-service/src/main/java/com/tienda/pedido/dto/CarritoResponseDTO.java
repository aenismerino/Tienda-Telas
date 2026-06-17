package com.tienda.pedido.dto;

import java.util.List;

import lombok.Data;


@Data
public class CarritoResponseDTO {
    private List<CarritoItemDTO> items;
    private Integer totalCarrito; 
    private Integer totalPagar;

    
}
