package com.tienda.carrito.dto;

import java.util.List;
import lombok.Data;

@Data
public class CarritoResponseDTO {
    private List<CarritoItemDTO> items;
    private Integer totalCarrito; 
    private Integer totalPagar;
    private Integer iva;
    private Integer descuento;
}
