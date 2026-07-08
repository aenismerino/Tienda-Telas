package com.auth_service.auth_service.controller;

import com.auth_service.auth_service.DTO.UserDTO;
import com.auth_service.auth_service.assemblers.UserModelAssembler;
import com.auth_service.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/auth/v2")
@RequiredArgsConstructor
public class AuthControllerV2 {

    private final AuthService authService;
    private final UserModelAssembler assembler;

    @GetMapping("/users")
    public ResponseEntity<?> listarUsuariosV2() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();

        if (role.equals("ROLE_COMPRADOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado para compradores");
        }

        log.info("V2 GET /auth/v2/users - Listando usuarios con HATEOAS");
        List<EntityModel<UserDTO>> userModels = authService.obtenerUsuarios().stream()
                .map(UserDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserDTO>> collectionModel = CollectionModel.of(userModels)
                .add(linkTo(methodOn(AuthControllerV2.class).listarUsuariosV2()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/users/{rut}")
    public ResponseEntity<?> obtenerUsuarioPorRutV2(@PathVariable String rut) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();

        if (role.equals("ROLE_COMPRADOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado para compradores");
        }

        log.info("V2 GET /auth/v2/users/{} - Buscando usuario con HATEOAS", rut);
        return authService.buscarPorRut(rut)
                .map(UserDTO::fromModel)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
