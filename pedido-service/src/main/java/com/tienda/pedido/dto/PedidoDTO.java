package com.tienda.pedido.dto;

import com.tienda.pedido.model.Pedido;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {

    private Integer id;

    @NotNull(message = "El ID del producto es obligatorio")
    private Integer productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 1, message = "El precio unitario debe ser mayor a 0")
    private Integer precioUnitario;

    private Integer totalPedido;
    private LocalDateTime fechaPedido;

    public Pedido toModel() {
        Pedido p = new Pedido();
        p.setId(this.id);
        p.setProductoId(this.productoId);
        p.setCantidad(this.cantidad);
        p.setPrecioUnitario(this.precioUnitario);
        p.setTotalPedido(this.totalPedido);
        p.setFechaPedido(this.fechaPedido);
        return p;
    }

    public static PedidoDTO fromModel(Pedido p) {
        if (p == null)
            return null;
        return PedidoDTO.builder()
                .id(p.getId())
                .productoId(p.getProductoId())
                .cantidad(p.getCantidad())
                .precioUnitario(p.getPrecioUnitario())
                .totalPedido(p.getTotalPedido())
                .fechaPedido(p.getFechaPedido())
                .build();
    }
}
