package com.tienda.pedido.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventario-service", url = "http://localhost:8083/productos")
public interface InventarioClient {

    @GetMapping("/{id}")
    Object obtenerProductoPorId(@PathVariable("id") Integer id);

}
