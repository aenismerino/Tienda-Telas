package com.envio_service.envio_service.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrackingDTO {

    @NotBlank(message = "La descripción no puede estar vacía")
    private String nuevaDescripcion;

}
