package com.tallermecanico.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Data;

// IMPORTAR Phone
// Como está en el mismo paquete, no hace falta import
@Data
@Entity
@Table(name = "cliente") // <--- Forzamos el nombre de la tabla
@EntityListeners(AuditingEntityListener.class) // Obligatorio para activar @CreatedDate
public class ClienteEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
    private Long id; // ID autogenerado
    @Column(name = "account_number", unique = true)
    private String accountNumber;
    private String clientType;
    private String firstName;
    private String lastName;
    private String docType;
    private String documentNumber;
    private LocalDate birthDate;
    private String clientGroup;
    private String currency;
    private String conditions;
    private String pais;
    private String provincia;
    private String ciudad;
    private String street;
    private String zipCode;
    private String licenseId;
    @Column(name = "cedula", nullable = false, unique = true)
    private String cedula;
    private String email;
    @Column(name = "cuit", nullable = false, unique = true)
    private String cuit;
    private String condicionIva;
    private boolean remindBirthday;
    private String observacion;
    @CreatedDate
    @Column(name = "fecha_alta", updatable = false)
    private LocalDateTime fechaAlta;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;


 // Relaciones
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id") // Relación limpia sin tabla intermedia
    private List<PhoneEntity> phones;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id") // Relación limpia sin tabla intermedia
    private List<VehiculoEntity> vehiculosAsociados;

    // Método para generar accountNumber
    public void generarAccountNumber(long contador) {
        String prefijo = this.clientType.equalsIgnoreCase("Empresa") ? "E-" : "I-";
        this.accountNumber = prefijo + String.format("%04d", contador);
    }
    
 
    
    
}

