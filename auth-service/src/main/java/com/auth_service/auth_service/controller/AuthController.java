package com.auth_service.auth_service.controller;

import com.auth_service.auth_service.DTO.UserDTO;
import com.auth_service.auth_service.model.User;
import com.auth_service.auth_service.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
    public ResponseEntity<?> listarUsuarios(){
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();

        if (role.equals("ROLE_COMPRADOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado para compradores");
        }

        log.info("Obteniendo todos los usuarios");
        List<User> users = authService.obtenerUsuarios();

        if (role.equals("ROLE_ADMIN")) {
            List<EntityModel<User>> userModels = users.stream()
                    .map(u -> EntityModel.of(u,
                            linkTo(methodOn(AuthController.class).obtenerUsuarioPorRut(u.getRut())).withSelfRel(),
                            linkTo(methodOn(AuthController.class).listarUsuarios()).withRel("users")))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(userModels, linkTo(methodOn(AuthController.class).listarUsuarios()).withSelfRel()));
        } else if (role.equals("ROLE_VENDEDOR")) {
            List<EntityModel<UserDTO>> dtoList = users.stream().map(u -> {
                UserDTO dto = new UserDTO();
                dto.setRut(u.getRut());
                dto.setNombres(u.getNombres());
                dto.setApellidos(u.getApellidos());
                dto.setCorreo(u.getCorreo());
                dto.setTelefono(u.getTelefono());
                dto.setPassword(null); // Ocultar contraseña
                dto.setRoleId(u.getRole() != null ? u.getRole().getId() : null);
                return EntityModel.of(dto,
                        linkTo(methodOn(AuthController.class).obtenerUsuarioPorRut(dto.getRut())).withSelfRel(),
                        linkTo(methodOn(AuthController.class).listarUsuarios()).withRel("users"));
            }).toList();
            return ResponseEntity.ok(CollectionModel.of(dtoList, linkTo(methodOn(AuthController.class).listarUsuarios()).withSelfRel()));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/users/{rut}")
    public ResponseEntity<?> obtenerUsuarioPorRut(@PathVariable String rut){
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();

        if (role.equals("ROLE_COMPRADOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado para compradores");
        }

        log.info("Buscando al usuario con RUT: {}", rut);
        return authService.buscarPorRut(rut)
                .map(u -> {
                    if (role.equals("ROLE_ADMIN")) {
                        return ResponseEntity.ok(EntityModel.of(u,
                                linkTo(methodOn(AuthController.class).obtenerUsuarioPorRut(rut)).withSelfRel(),
                                linkTo(methodOn(AuthController.class).listarUsuarios()).withRel("users")));
                    } else {
                        UserDTO dto = new UserDTO();
                        dto.setRut(u.getRut());
                        dto.setNombres(u.getNombres());
                        dto.setApellidos(u.getApellidos());
                        dto.setCorreo(u.getCorreo());
                        dto.setTelefono(u.getTelefono());
                        dto.setPassword(null);
                        dto.setRoleId(u.getRole() != null ? u.getRole().getId() : null);
                        return ResponseEntity.ok(EntityModel.of(dto,
                                linkTo(methodOn(AuthController.class).obtenerUsuarioPorRut(rut)).withSelfRel(),
                                linkTo(methodOn(AuthController.class).listarUsuarios()).withRel("users")));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<User> registrarUsuario(@RequestBody UserDTO userDTO){
        log.info("Iniciando el registro de usuario con RUT: {}", userDTO.getRut());
        try {
            User registrado = authService.registrarUsuario(userDTO);
            log.info("Usuario registrado exitosamente con RUT: {}", registrado.getRut());
            return new  ResponseEntity<>(registrado, HttpStatus.CREATED);
        } catch ( RuntimeException e ){
            log.error("Error al registrar usuario: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/users/{rut}")
    public ResponseEntity<User> actualizarUsuario(@PathVariable String rut, @RequestBody UserDTO userDTO) {
        log.info("Actualizando usuario con RUT: {}", rut);
        try {
            return ResponseEntity.ok(authService.actualizarUsuario(rut, userDTO));
        } catch (RuntimeException e) {
            log.error("Error al actualizar usuario: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/users/{rut}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String rut) {
        log.info("Eliminando usuario con RUT: {}", rut);
        try {
            authService.eliminarUsuario(rut);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
