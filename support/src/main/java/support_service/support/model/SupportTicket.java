package support_service.support.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "support_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de usuario es obligatorio")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_id")
    private Long orderId;

    @NotBlank(message = "El asunto no puede estar vacío")
    @Size(max = 100, message = "El asunto no puede superar los 100 caracteres")
    private String subject;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Column(length = 500)
    private String description;

    @NotBlank(message = "El estado es obligatorio")
    private String status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    // --- CONSTRUCTORES EXPLÍCITOS NATIVOS ANTI-BUG ---
    public SupportTicket() {
    }

    public SupportTicket(Long id, Long userId, Long orderId, String subject, String description, String status, LocalDate createdAt) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        if (this.status == null) {
            this.status = "PENDIENTE";
        }
    }

    // --- GETTERS Y SETTERS MANUALES ---
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getSubject() { return this.subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}