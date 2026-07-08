package com.catalogo_service.catalogo_service.service;

import org.springframework.stereotype.Service;
import com.catalogo_service.catalogo_service.client.InventarioClient;
import com.catalogo_service.catalogo_service.client.ResenasClient;
import com.catalogo_service.catalogo_service.DTO.CatalogoResponseDto;
import com.catalogo_service.catalogo_service.DTO.ProductoDto;
import com.catalogo_service.catalogo_service.DTO.ResenaDto;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final InventarioClient inventarioClient;
    private final ResenasClient resenasClient;

    public CatalogoResponseDto obtenerCatalogoDeTela(Long telaId) {
        ProductoDto producto = inventarioClient.obtenerProductoPorId(telaId);

        List<ResenaDto> todasLasResenas = resenasClient.obtenerTodasLasResenas();
        List<ResenaDto> resenasDeEstaTela = todasLasResenas.stream()
                .filter(r -> r.getTelaId() != null && r.getTelaId().equals(telaId))
                .collect(Collectors.toList());

        double promedio = resenasDeEstaTela.stream()
                .mapToInt(ResenaDto::getCalificacion)
                .average()
                .orElse(0.0);

        return CatalogoResponseDto.builder()
                .producto(producto)
                .resenas(resenasDeEstaTela)
                .promedioEstrellas(Math.round(promedio * 10.0) / 10.0)
                .build();
    }
}
