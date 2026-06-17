package com.tienda.pedido.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pago-service", url = "http://localhost:8084/payments")
public interface PagoClient {

    @PostMapping
    Object procesarPago(@RequestBody Object paymentDTO);

}
