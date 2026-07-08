package com.notificiones_service.notificiones_service.DTO;

import com.notificiones_service.notificiones_service.model.Notificacion;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {

    private Long id;

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;

    @NotBlank(message = "El destinatario no puede estar vacío")
    private String destinatario;

    private String estado;
    private LocalDateTime fecha;

    public Notificacion toModel() {
        return Notificacion.builder()
                .mensaje(this.mensaje)
                .destinatario(this.destinatario)
                .build();
    }

    public static NotificacionDTO fromModel(Notificacion n) {
        if (n == null) return null;
        return NotificacionDTO.builder()
                .id(n.getId())
                .mensaje(n.getMensaje())
                .destinatario(n.getDestinatario())
                .estado(n.getEstado())
                .fecha(n.getFecha())
                .build();
    }
}
