package com.auth_service.auth_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.auth_service.auth_service.DTO.UserDTO;
import com.auth_service.auth_service.model.Role;
import com.auth_service.auth_service.model.User;
import com.auth_service.auth_service.service.AuthService;
import com.auth_service.auth_service.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user;
    private UserDTO userDTO;
    private Role role;

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

        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testLoginCorrecto() throws Exception {
        when(authService.buscarPorRut("11111111-1")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("11111111-1", "ROLE_ADMIN")).thenReturn("fake-jwt-token");

        Map<String, String> creds = new HashMap<>();
        creds.put("rut", "11111111-1");
        creds.put("password", "1234");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk())
                .andExpect(content().string("fake-jwt-token"));
    }

    @Test
    public void testLoginIncorrecto() throws Exception {
        when(authService.buscarPorRut("11111111-1")).thenReturn(Optional.of(user));

        Map<String, String> creds = new HashMap<>();
        creds.put("rut", "11111111-1");
        creds.put("password", "wrong-password");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciales invalidas"));
    }

    @Test
    public void testRegistrarUsuario() throws Exception {
        when(authService.registrarUsuario(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rut").value("11111111-1"))
                .andExpect(jsonPath("$.nombres").value("Juan"));
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        doNothing().when(authService).eliminarUsuario("11111111-1");

        mockMvc.perform(delete("/auth/users/11111111-1"))
                .andExpect(status().isNoContent());

        verify(authService, times(1)).eliminarUsuario("11111111-1");
    }
}
