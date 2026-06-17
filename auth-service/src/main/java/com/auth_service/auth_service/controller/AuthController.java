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
                .map(u -> ResponseEntity.ok(jwtUtil.generateToken(u.getRut())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas"));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> listarUsuarios(){
        log.info("Obteniendo todos los usuarios");
        return ResponseEntity.ok(authService.obtenerUsuarios());
    }

    @GetMapping("/users/{rut}")
    public ResponseEntity<User> obtenerUsuarioPorRut(@PathVariable String rut){
        log.info("Buscando al usuario con RUT: {}", rut);
        return authService.buscarPorRut(rut)
                .map(user -> ResponseEntity.ok(user))
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
