package com.tallermecanico.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class VehiculoDTO {
    private String vehicleId;
    private String brand;
    private String model;
    private String licensePlate;
}
