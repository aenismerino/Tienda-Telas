package com.auth_service.auth_service.controller;

import com.auth_service.auth_service.DTO.UserDTO;
import com.auth_service.auth_service.model.User;
import com.auth_service.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private com.auth_service.auth_service.util.JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody java.util.Map<String, String> credentials) {
        String rut = credentials.get("rut");
        String password = credentials.get("password");

        log.info("Intento de login para RUT: {}", rut);

        return authService.buscarPorRut(rut)
                .filter(u -> u.getPassword().equals(password))
                .map(u -> ResponseEntity.ok(jwtUtil.generateToken(u.getRut(), u.getRole().getNombre())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas"));
    }

    @GetMapping("/users")
    public ResponseEntity<?> listarUsuarios() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();

        if (role.equals("ROLE_COMPRADOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado para compradores");
        }

        log.info("Obteniendo todos los usuarios");
        List<User> users = authService.obtenerUsuarios();

        if (role.equals("ROLE_ADMIN")) {
            return ResponseEntity.ok(users);
        } else if (role.equals("ROLE_VENDEDOR")) {
            List<UserDTO> dtoList = users.stream().map(UserDTO::fromModel).collect(Collectors.toList());
            return ResponseEntity.ok(dtoList);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/users/{rut}")
    public ResponseEntity<?> obtenerUsuarioPorRut(@PathVariable String rut) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();

        if (role.equals("ROLE_COMPRADOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado para compradores");
        }

        log.info("Buscando al usuario con RUT: {}", rut);
        return authService.buscarPorRut(rut)
                .map(u -> {
                    if (role.equals("ROLE_ADMIN")) {
                        return ResponseEntity.ok(u);
                    } else {
                        return ResponseEntity.ok(UserDTO.fromModel(u));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registrarUsuario(@Valid @RequestBody UserDTO userDTO) {
        log.info("Iniciando el registro de usuario con RUT: {}", userDTO.getRut());
        User registrado = authService.registrarUsuario(userDTO);
        log.info("Usuario registrado exitosamente con RUT: {}", registrado.getRut());
        return new ResponseEntity<>(UserDTO.fromModel(registrado), HttpStatus.CREATED);
    }

    @PutMapping("/users/{rut}")
    public ResponseEntity<UserDTO> actualizarUsuario(@PathVariable String rut, @Valid @RequestBody UserDTO userDTO) {
        log.info("Actualizando usuario con RUT: {}", rut);
        User actualizado = authService.actualizarUsuario(rut, userDTO);
        return ResponseEntity.ok(UserDTO.fromModel(actualizado));
    }

    @DeleteMapping("/users/{rut}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String rut) {
        log.info("Eliminando usuario con RUT: {}", rut);
        authService.eliminarUsuario(rut);
        return ResponseEntity.noContent().build();
    }

}
