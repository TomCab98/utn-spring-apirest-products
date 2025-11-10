package com.utn.productos_api.mapper;

import com.utn.productos_api.dto.ProductoDTO;
import com.utn.productos_api.dto.ProductoResponseDTO;
import com.utn.productos_api.model.Producto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
  ProductoDTO toProductoDTO(Producto model);
  ProductoResponseDTO toProductoResponseDTO(Producto model);
  Producto toProducto(ProductoDTO dto);
  List<ProductoDTO> toProductoDTOList(List<Producto> models);
  List<ProductoResponseDTO> toProductoResponseDTOList(List<Producto> models);
}
