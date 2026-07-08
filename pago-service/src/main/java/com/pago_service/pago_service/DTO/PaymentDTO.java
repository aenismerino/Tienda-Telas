package com.pago_service.pago_service.DTO;

import com.pago_service.pago_service.model.Payment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long id;

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long orderId;

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    private Integer monto;

    private String estado;
    private LocalDateTime fechaPago;

    public Payment toModel() {
        Payment p = new Payment();
        p.setOrderId(this.orderId);
        p.setMonto(this.monto);
        return p;
    }

    public static PaymentDTO fromModel(Payment p) {
        if (p == null) return null;
        return PaymentDTO.builder()
                .id(p.getId())
                .orderId(p.getOrderId())
                .monto(p.getMonto())
                .estado(p.getEstado())
                .fechaPago(p.getFechaPago())
                .build();
    }
}
