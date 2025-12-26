package com.tallermecanico.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class ClienteDTO {
	

    private String accountNumber;
    @NotBlank(message = "El tipo de cliente es obligatorio")
    private String clientType;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String firstName;
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;
    private String docType;
    @NotBlank(message = "El número de documento es obligatorio")
    @Pattern(regexp = "\\d+", message = "El número de documento debe ser solo números")
    private String documentNumber;
    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato YYYY-MM-DD")
    private String birthDate;
    private String clientGroup;
    private String currency;
    private String conditions;
    private String pais;
    private String provincia;
    private String ciudad;
    private String street;
    private String zipCode;
    private String licenseId;
    @Email(message = "Email inválido")
    private String email;
    @Pattern(regexp = "\\d{11}", message = "CUIT debe tener 11 dígitos")
    private String cuit;
    private String condicionIva;
    private boolean remindBirthday;
    private String observacion;
    @NotBlank(message = "La cédula es obligatoria")
    private String cedula;
    // Campos relacionados
    private List<PhoneDTO> phones;
    private List<VehiculoDTO> vehiculosAsociados;
    private LocalDateTime fechaAlta;
    private LocalDateTime fechaModificacion;
  
}

