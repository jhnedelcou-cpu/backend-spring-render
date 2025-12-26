package com.tallermecanico.entity;



import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "phone") // <--- Forzamos el nombre de la tab
public class PhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;      // "celular" | "fijo"
    private String areaCode;
    private String number;
}
