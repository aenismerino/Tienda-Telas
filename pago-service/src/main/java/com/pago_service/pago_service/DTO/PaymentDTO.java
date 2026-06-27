package com.pago_service.pago_service.DTO;

import lombok.Data;

@Data
public class PaymentDTO {

    private Long orderId;
    private Integer monto;

}
