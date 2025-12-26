package com.tallermecanico.entity;



import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehiculo") // <--- Forzamos el nombre de la tab
public class VehiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleId;
}

