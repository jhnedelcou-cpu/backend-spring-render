package com.tallermecanico.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClienteResponseDTO {

    private Long id;
    private String accountNumber;
    private String clientType;
    private String firstName;
    private String lastName;
    private String docType;
    private String documentNumber;
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
    private String email;
    private String cuit;
    private String condicionIva;
    private boolean remindBirthday;
    private String observacion;
    private String cedula;

    private List<PhoneDTO> phones;
    private List<VehiculoDTO> vehiculosAsociados;
    private LocalDateTime fechaAlta;
    private LocalDateTime fechaModificacion;
}

