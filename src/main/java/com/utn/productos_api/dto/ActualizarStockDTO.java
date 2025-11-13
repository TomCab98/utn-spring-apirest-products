package com.utn.productos_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para actualización parcial del stock de un producto. Permite modificar únicamente la cantidad en inventario sin enviar todos los datos del producto.")
public class ActualizarStockDTO {

  @Schema(
      description = "Nueva cantidad de stock disponible del producto. Debe ser un número entero mayor o igual a cero.",
      example = "50",
      required = true,
      minimum = "0",
      type = "integer"
  )
  @NotNull(message = "El stock no puede ser nulo")
  @Min(value = 1, message = "El stock no puede ser inferior a 0")
  Integer stock;
}