package com.pago_service.pago_service.controller;

import com.pago_service.pago_service.DTO.PaymentDTO;
import com.pago_service.pago_service.assemblers.PaymentModelAssembler;
import com.pago_service.pago_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/payments/v2")
@RequiredArgsConstructor
public class PaymentControllerV2 {

    private final PaymentService paymentService;
    private final PaymentModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PaymentDTO>>> listarPagosV2() {
        log.info("V2 GET /payments/v2 - Listando pagos con HATEOAS");
        List<EntityModel<PaymentDTO>> payments = paymentService.obtenerTodos().stream()
                .map(PaymentDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PaymentDTO>> collectionModel = CollectionModel.of(payments)
                .add(linkTo(methodOn(PaymentControllerV2.class).listarPagosV2()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PaymentDTO>> obtenerPagoV2(@PathVariable Long id) {
        log.info("V2 GET /payments/v2/{} - Buscando pago con HATEOAS", id);
        return paymentService.obtenerPorId(id)
                .map(PaymentDTO::fromModel)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
