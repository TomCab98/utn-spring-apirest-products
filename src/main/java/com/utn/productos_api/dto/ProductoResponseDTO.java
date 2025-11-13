package com.utn.productos_api.dto;

import com.utn.productos_api.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO de respuesta que contiene la información completa de un producto, incluyendo su ID generado por el sistema")
public class ProductoResponseDTO {

  @Schema(
      description = "Identificador único del producto generado automáticamente por el sistema",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY
  )
  Long id;

  @Schema(
      description = "Nombre del producto",
      example = "Laptop Dell Inspiron 15"
  )
  String nombre;

  @Schema(
      description = "Descripción detallada del producto con características y especificaciones",
      example = "Laptop con procesador Intel Core i5 de 11va generación, 8GB RAM DDR4, 256GB SSD, pantalla 15.6 pulgadas Full HD",
      nullable = true
  )
  String descripcion;

  @Schema(
      description = "Precio unitario del producto en la moneda local",
      example = "45999.99",
      type = "number",
      format = "double"
  )
  Double precio;

  @Schema(
      description = "Cantidad disponible en inventario",
      example = "25",
      type = "integer"
  )
  Integer stock;

  @Schema(
      description = "Categoría del producto",
      example = "ELECTRONICA",
      allowableValues = {"ELECTRONICA", "ROPA", "ALIMENTOS", "HOGAR", "DEPORTES"}
  )
  Categoria categoria;
}