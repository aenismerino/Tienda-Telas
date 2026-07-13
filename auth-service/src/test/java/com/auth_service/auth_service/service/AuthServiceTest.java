package com.auth_service.auth_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.auth_service.auth_service.DTO.UserDTO;
import com.auth_service.auth_service.model.Role;
import com.auth_service.auth_service.model.User;
import com.auth_service.auth_service.repository.RoleRepository;
import com.auth_service.auth_service.repository.UserRepository;
import com.auth_service.auth_service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private User user;
    private Role role;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setNombre("ROLE_ADMIN");

        user = new User();
        user.setRut("11111111-1");
        user.setNombres("Juan");
        user.setApellidos("Perez");
        user.setCorreo("juan@test.com");
        user.setPassword("1234");
        user.setRole(role);

        userDTO = UserDTO.builder()
                .rut("11111111-1")
                .nombres("Juan")
                .apellidos("Perez")
                .correo("juan@test.com")
                .password("1234")
                .roleId(1L)
                .build();
    }

    @Test
    void testObtenerUsuarios() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> users = authService.obtenerUsuarios();
        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void testBuscarPorRut() {
        when(userRepository.findById("11111111-1")).thenReturn(Optional.of(user));
        Optional<User> resultado = authService.buscarPorRut("11111111-1");
        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombres());
        verify(userRepository).findById("11111111-1");
    }

    @Test
    void testRegistrarUsuario() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User resultado = authService.registrarUsuario(userDTO);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombres());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testActualizarUsuario() {
        when(userRepository.findById("11111111-1")).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        User actualizado = new User();
        actualizado.setRut("11111111-1");
        actualizado.setNombres("Modificado");
        actualizado.setRole(role);
        when(userRepository.save(any(User.class))).thenReturn(actualizado);

        UserDTO dtoUpdate = UserDTO.builder()
                .nombres("Modificado")
                .roleId(1L)
                .build();

        User resultado = authService.actualizarUsuario("11111111-1", dtoUpdate);
        assertNotNull(resultado);
        assertEquals("Modificado", resultado.getNombres());
    }

    @Test
    void testEliminarUsuario() {
        when(userRepository.existsById("11111111-1")).thenReturn(true);
        doNothing().when(userRepository).deleteById("11111111-1");

        assertDoesNotThrow(() -> authService.eliminarUsuario("11111111-1"));
        verify(userRepository, times(1)).deleteById("11111111-1");
    }

    @Test
    void testEliminarNoEncontrado() {
        when(userRepository.existsById("999")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> authService.eliminarUsuario("999"));
    }
}
