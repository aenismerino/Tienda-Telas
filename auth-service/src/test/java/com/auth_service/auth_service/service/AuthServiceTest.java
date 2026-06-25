package com.auth_service.auth_service.service;

import com.auth_service.auth_service.DTO.UserDTO;
import com.auth_service.auth_service.model.Role;
import com.auth_service.auth_service.model.User;
import com.auth_service.auth_service.repository.RoleRepository;
import com.auth_service.auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarUsuario_Exito() {
        // Given
        UserDTO dto = new UserDTO();
        dto.setRut("12345678-5"); // RUT Valido
        dto.setNombres("Juan");
        dto.setRoleId(1L);
        dto.setPassword("1234");

        Role role = new Role();
        role.setId(1L);

        User savedUser = new User();
        savedUser.setRut("12345678-5");
        savedUser.setNombres("Juan");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = authService.registrarUsuario(dto);

        // Then
        assertNotNull(result);
        assertEquals("12345678-5", result.getRut());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegistrarUsuario_RutInvalido() {
        // Given
        UserDTO dto = new UserDTO();
        dto.setRut("12345678-9"); // RUT Invalido

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(dto);
        });

        assertEquals("El RUT ingresado no es valido", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testBuscarPorRut_Exito() {
        // Given
        User user = new User();
        user.setRut("12345678-5");
        when(userRepository.findById("12345678-5")).thenReturn(Optional.of(user));

        // When
        Optional<User> result = authService.buscarPorRut("12345678-5");

        // Then
        assertTrue(result.isPresent());
        assertEquals("12345678-5", result.get().getRut());
    }
}
