package com.tienda.pedido.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "carrito_items")
@Data
public class CarritoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer usuarioId;   
    private Integer productoId;  
    private Integer cantidad;    
    private Integer precioUnitario; 
    private Boolean seleccionado;

    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "pedido_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Pedido pedido;
}
