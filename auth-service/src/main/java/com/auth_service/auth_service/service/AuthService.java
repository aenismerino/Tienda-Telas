package com.auth_service.auth_service.service;

import com.auth_service.auth_service.DTO.UserDTO;
import com.auth_service.auth_service.exception.BadRequestException;
import com.auth_service.auth_service.exception.ResourceNotFoundException;
import com.auth_service.auth_service.model.Role;
import com.auth_service.auth_service.model.User;
import com.auth_service.auth_service.repository.RoleRepository;
import com.auth_service.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> obtenerUsuarios() {
        return userRepository.findAll();
    }

    public Optional<User> buscarPorRut(String rut) {
        return userRepository.findById(rut);
    }

    public User registrarUsuario(UserDTO dto) {
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User nuevoUsuario = dto.toModel(role);
        return userRepository.save(nuevoUsuario);
    }

    public User actualizarUsuario(String rut, UserDTO dto) {
        User usuario = userRepository.findById(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con RUT: " + rut));

        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(dto.getPassword());
        }

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            usuario.setRole(role);
        }

        return userRepository.save(usuario);
    }

    public void eliminarUsuario(String rut) {
        if (!userRepository.existsById(rut)) {
            throw new ResourceNotFoundException("Usuario no encontrado con RUT: " + rut);
        }
        userRepository.deleteById(rut);
    }

}
