package com.catalogo_service.catalogo_service.client;

import com.catalogo_service.catalogo_service.DTO.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventario-service", url = "${inventario.url}")
public interface InventarioClient {
    @GetMapping("/{id}")
    ProductoDto obtenerProductoPorId(@PathVariable("id") Long id);

}
