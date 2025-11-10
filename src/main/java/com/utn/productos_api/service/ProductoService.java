package com.utn.productos_api.service;

import com.utn.productos_api.dto.ActualizarStockDTO;
import com.utn.productos_api.dto.ProductoDTO;
import com.utn.productos_api.dto.ProductoResponseDTO;
import com.utn.productos_api.exceptions.ProductoNoEncontradoException;
import com.utn.productos_api.mapper.ProductoMapper;
import com.utn.productos_api.model.Categoria;
import com.utn.productos_api.model.Producto;
import com.utn.productos_api.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

  private final ProductoRepository productoRepository;
  private final ProductoMapper mapper;

  public ProductoService(ProductoRepository productoRepository, ProductoMapper mapper) {
    this.productoRepository = productoRepository;
    this.mapper = mapper;
  }

  public ProductoResponseDTO crearProducto(ProductoDTO dto) {
    Producto producto = mapper.toProducto(dto);
    return mapper.toProductoResponseDTO(productoRepository.save(producto));
  }

  public List<ProductoResponseDTO> obtenerTodos() {
    List<Producto> productos = productoRepository.findAll();
    return mapper.toProductoResponseDTOList(productos);
  }

  public ProductoResponseDTO obtenerPorId(Long id) {
    Optional<Producto> producto = productoRepository.findById(id);
    if (producto.isEmpty()) {
      throw new ProductoNoEncontradoException("No existe el producto con el id: " + id);
    }

    return mapper.toProductoResponseDTO(producto.get());
  }

  public List<ProductoResponseDTO> obtenerPorCategoria(Categoria categoria) {
    List<Producto> productos = productoRepository.findByCategoria(categoria);
    return mapper.toProductoResponseDTOList(productos);
  }

  public ProductoResponseDTO actualizarProducto(Long id, ProductoDTO dtoProductoActualizado) {
    Optional<Producto> producto = productoRepository.findById(id);
    if (producto.isEmpty()) {
      throw new ProductoNoEncontradoException("No existe el producto con el id: " + id);
    }

    Producto productoActualizado = mapper.toProducto(dtoProductoActualizado);
    productoActualizado.setId(id);

    return mapper.toProductoResponseDTO(productoRepository.save(productoActualizado));
  }

  public ProductoResponseDTO actualizarStock(Long id, ActualizarStockDTO stock) {
    Optional<Producto> producto = productoRepository.findById(id);
    if (producto.isEmpty()) {
      throw new ProductoNoEncontradoException("No existe el producto con el id: " + id);
    }

    Producto productoActualizado = producto.get();
    productoActualizado.setStock(stock.getStock());

    return mapper.toProductoResponseDTO(productoRepository.save(productoActualizado));
  }

  public void  eliminarProducto(Long id) {
    productoRepository.deleteById(id);
  }
}
