package com.tallermecanico.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class PhoneDTO {

    @NotBlank(message = "El tipo de teléfono es obligatorio")
    private String type;

    @NotBlank(message = "El código de área es obligatorio")
    @Pattern(regexp = "\\d{2,4}", message = "El código de área debe tener entre 2 y 4 dígitos")
    private String areaCode;

    @NotBlank(message = "El número es obligatorio")
    @Pattern(regexp = "\\d{6,10}", message = "El número debe tener entre 6 y 10 dígitos")
    private String number;
}



