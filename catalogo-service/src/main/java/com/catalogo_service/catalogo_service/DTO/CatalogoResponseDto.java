package com.catalogo_service.catalogo_service.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CatalogoResponseDto {
    private ProductoDto producto;
    private List<ResenaDto> resenas;
    private double promedioEstrellas;
}
