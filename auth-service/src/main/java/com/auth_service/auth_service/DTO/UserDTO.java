package com.auth_service.auth_service.DTO;

import com.auth_service.auth_service.model.User;
import com.auth_service.auth_service.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @Email(message = "El correo debe tener un formato valido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    private String telefono;
    private String password;
    private Long roleId;

    public User toModel(Role role) {
        User user = new User();
        user.setRut(this.rut);
        user.setNombres(this.nombres);
        user.setApellidos(this.apellidos);
        user.setCorreo(this.correo);
        user.setTelefono(this.telefono);
        user.setPassword(this.password);
        user.setRole(role);
        return user;
    }

    public static UserDTO fromModel(User u) {
        if (u == null)
            return null;
        return UserDTO.builder()
                .rut(u.getRut())
                .nombres(u.getNombres())
                .apellidos(u.getApellidos())
                .correo(u.getCorreo())
                .telefono(u.getTelefono())
                .password(null)
                .roleId(u.getRole() != null ? u.getRole().getId() : null)
                .build();
    }

}
