package com.envio_service.envio_service.DTO;

import com.envio_service.envio_service.model.Shipment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDTO {

    private Long id;

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long orderId;

    @NotBlank(message = "La dirección de destino es obligatoria")
    private String direccionDestino;

    private String estadoActual;

    public Shipment toModel() {
        Shipment s = new Shipment();
        s.setOrderId(this.orderId);
        s.setDireccionDestino(this.direccionDestino);
        return s;
    }

    public static ShipmentDTO fromModel(Shipment s) {
        if (s == null) return null;
        return ShipmentDTO.builder()
                .id(s.getId())
                .orderId(s.getOrderId())
                .direccionDestino(s.getDireccionDestino())
                .estadoActual(s.getEstadoActual())
                .build();
    }
}
