package com.auth_service.auth_service.service;

import com.auth_service.auth_service.DTO.UserDTO;
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
        return  userRepository.findById(rut);
    }

    private boolean validarRut(String rut) {
        if (rut == null || !rut.matches("^[0-9]+-[0-9kK]{1}$")) return false;
        String[] partes = rut.split("-");
        String rutNumeros = partes[0];
        String dv = partes[1].toUpperCase();
        
        int suma = 0;
        int multiplicador = 2;
        for (int i = rutNumeros.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(rutNumeros.charAt(i)) * multiplicador;
            multiplicador = multiplicador == 7 ? 2 : multiplicador + 1;
        }
        
        int resto = suma % 11;
        int dvCalculado = 11 - resto;
        String dvEsperado;
        if (dvCalculado == 11) dvEsperado = "0";
        else if (dvCalculado == 10) dvEsperado = "K";
        else dvEsperado = String.valueOf(dvCalculado);
        
        return dv.equals(dvEsperado);
    }

    public User registrarUsuario(UserDTO dto) {
        if (!validarRut(dto.getRut())) {
            throw new RuntimeException("El RUT ingresado no es valido");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User nuevoUsuario = new User();
        nuevoUsuario.setRut(dto.getRut());
        nuevoUsuario.setNombres(dto.getNombres());
        nuevoUsuario.setApellidos(dto.getApellidos());
        nuevoUsuario.setCorreo(dto.getCorreo());
        nuevoUsuario.setTelefono(dto.getTelefono());
        nuevoUsuario.setPassword(dto.getPassword());
        nuevoUsuario.setRole(role);

        return userRepository.save(nuevoUsuario);
    }

    public User actualizarUsuario(String rut, UserDTO dto) {
        User usuario = userRepository.findById(rut)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
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
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(rut);
    }

}
