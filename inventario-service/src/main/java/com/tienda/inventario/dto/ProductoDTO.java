package com.tienda.inventario.dto;

import com.tienda.inventario.model.Producto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {

    private Integer id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo del producto es obligatorio")
    private String tipo;

    private String descripcion;

    @NotNull(message = "El precio del producto es obligatorio")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private Integer precio;

    @NotNull(message = "El stock del producto es obligatorio")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    private Integer stock;

    @NotNull(message = "Los metros deben ser obligatorios")
    @Min(value = 0, message = "Los metros deben ser mayor o igual a 0")
    private Double metros;

    public Producto toModel() {
        Producto p = new Producto();
        p.setId(this.id);
        p.setNombre(this.nombre);
        p.setTipo(this.tipo);
        p.setDescripcion(this.descripcion);
        p.setPrecio(this.precio);
        p.setStock(this.stock);
        p.setMetros(this.metros);
        return p;
    }

    public static ProductoDTO fromModel(Producto p) {
        if (p == null)
            return null;
        return ProductoDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .tipo(p.getTipo())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .metros(p.getMetros())
                .build();
    }
}
