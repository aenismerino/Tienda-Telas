package com.envio_service.envio_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EnvioServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnvioServiceApplication.class, args);
	}

}
