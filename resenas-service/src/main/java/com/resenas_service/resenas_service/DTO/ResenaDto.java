package com.resenas_service.resenas_service.DTO;

import java.time.LocalDate;

import com.resenas_service.resenas_service.model.Resena;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResenaDto {
    @NotNull(message = "El ID de la tela es obligatorio")
    private Long telaId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "Lacalificacion minima es de 1")
    @Max(value = 5, message = "La calificacion maxima es de 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario no puede estar vacio")
    @Size(max = 300, message = "El comentario maximo son 300 caracteres")
    private String comentario;

    public Resena toModel() {
        return new Resena(null, telaId, usuarioId, calificacion, comentario, LocalDate.now());
    }

    public static ResenaDto fromModel(Resena r) {
        if (r == null)
            return null;
        return new ResenaDto(r.getTelaId(), r.getUsuarioId(), r.getCalificacion(), r.getComentario());
    }
}
