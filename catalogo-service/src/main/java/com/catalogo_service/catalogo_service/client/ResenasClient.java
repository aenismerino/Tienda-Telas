package com.catalogo_service.catalogo_service.client;

import com.catalogo_service.catalogo_service.DTO.ResenaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "resenas-service", url = "${resenas.url}")
public interface ResenasClient {

    @GetMapping
    List<ResenaDto> obtenerTodasLasResenas();

}
