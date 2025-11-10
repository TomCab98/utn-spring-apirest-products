package com.utn.productos_api.dto;

import com.utn.productos_api.model.Categoria;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoResponseDTO {
  Long id;
  String nombre;
  String descripcion;
  Double precio;
  Integer stock;
  Categoria categoria;
}
