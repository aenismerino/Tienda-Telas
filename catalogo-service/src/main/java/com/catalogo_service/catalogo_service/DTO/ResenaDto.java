package com.catalogo_service.catalogo_service.DTO;

import lombok.Data;

@Data
public class ResenaDto {
    private Long telaId;
    private Integer calificacion;
    private String comentario;
}
