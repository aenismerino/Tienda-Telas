package com.pago_service.pago_service.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pedido-service", url = "http://localhost:8085/pedidos")
public interface OrderClient {

    @PutMapping("/{id}/estado")
    void actualizarEstadoPedido(@PathVariable Long id, @RequestParam("estado") String estado);

}
