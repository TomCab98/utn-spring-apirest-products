package com.utn.productos_api.dto;

import com.utn.productos_api.model.Categoria;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductoDTO {

  @NotBlank(message = "El nombre no debe ser nulo o vacio")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
  String nombre;

  @Size(max = 500, message = "Limite excedido - Caracteres permitidos: 500")
  String descripcion;

  @NotNull(message = "El precio no puede ser nulo")
  @DecimalMin(value = "0.01", message = "El valor no puede ser inferior a $0,01")
  Double precio;

  @NotNull(message = "El stock no puede ser nulo")
  @Size(min = 0, message = "El stock no puede ser inferior a 0")
  Integer stock;

  @NotNull(message = "La categoria no puede ser nula")
  Categoria categoria;
}
