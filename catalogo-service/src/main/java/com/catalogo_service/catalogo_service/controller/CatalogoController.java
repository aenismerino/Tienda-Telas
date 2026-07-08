package com.catalogo_service.catalogo_service.controller;

import com.catalogo_service.catalogo_service.DTO.CatalogoResponseDto;
import com.catalogo_service.catalogo_service.service.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;

    @GetMapping("/{telaId}")
    public ResponseEntity<CatalogoResponseDto> verDetalleDeTela(@PathVariable Long telaId) {
        return ResponseEntity.ok(catalogoService.obtenerCatalogoDeTela(telaId));
    }

}
