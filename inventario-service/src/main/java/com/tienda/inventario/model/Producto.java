package com.tienda.inventario.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Producto {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private Integer precio;
    private Integer stock;
    private Double metros;
}
