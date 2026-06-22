package support_service.support.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicketDTO {

    @NotNull(message = "El ID de usuario (userId) es obligatorio.")
    private Long userId;

    private Long orderId;

    @NotBlank(message = "El asunto (subject) no puede estar vacío.")
    @Size(max = 100, message = "El asunto no puede tener más de 100 caracteres.")
    private String subject;

    @NotBlank(message = "La descripción (description) no puede estar vacía.")
    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres.")
    private String description;

    // --- MÉTODOS MANUALES OBLIGATORIOS PARA EVITAR EL BUG DE TU JAVA 21 ---
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getSubject() { return this.subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
}
