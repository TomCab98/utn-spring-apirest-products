package com.utn.productos_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActualizarStockDTO {

  @NotNull(message = "El stock no puede ser nulo")
  @Size(message = "El stock no puede ser inferior a 0")
  Integer stock;
}
