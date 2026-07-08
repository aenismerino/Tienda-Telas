package com.support_service.support_service.DTO;

import com.support_service.support_service.model.Ticket;
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
public class TicketDTO {

    private Long id;

    @NotBlank(message = "El asunto no puede estar vacío")
    private String asunto;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    private String estado;
    private LocalDateTime fecha;

    public Ticket toModel() {
        return Ticket.builder()
                .asunto(this.asunto)
                .descripcion(this.descripcion)
                .build();
    }

    public static TicketDTO fromModel(Ticket t) {
        if (t == null) return null;
        return TicketDTO.builder()
                .id(t.getId())
                .asunto(t.getAsunto())
                .descripcion(t.getDescripcion())
                .estado(t.getEstado())
                .fecha(t.getFecha())
                .build();
    }
}
