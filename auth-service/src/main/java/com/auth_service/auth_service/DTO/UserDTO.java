package com.auth_service.auth_service.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private String rut;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String password;
    private Long roleId;

}
