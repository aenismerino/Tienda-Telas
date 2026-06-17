package com.envio_service.envio_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pedido-service", url = "http://localhost:8085/pedidos")
public interface PedidoClient {

    @GetMapping("/{id}")
    Object obtenerPedidoPorId(@PathVariable("id") Long id);

}
