package com.utn.productos_api.controller;

import com.utn.productos_api.dto.ActualizarStockDTO;
import com.utn.productos_api.dto.ProductoDTO;
import com.utn.productos_api.dto.ProductoResponseDTO;
import com.utn.productos_api.model.Categoria;
import com.utn.productos_api.service.ProductoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/productos")
public class ProductoController {
  private final ProductoService productoService;

  public ProductoController(ProductoService productoService) {
    this.productoService = productoService;
  }

  @GetMapping
  public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
    List<ProductoResponseDTO> productos = productoService.obtenerTodos();
    return ResponseEntity.ok(productos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
    ProductoResponseDTO producto = productoService.obtenerPorId(id);
    return ResponseEntity.ok(producto);
  }

  @GetMapping("/categoria/{categoria}")
  public ResponseEntity<List<ProductoResponseDTO>> filtrarPorCategoria(@PathVariable String categoria) {
    List<ProductoResponseDTO> productos = productoService.obtenerPorCategoria(
        Categoria.valueOf(categoria));
    return ResponseEntity.ok(productos);
  }

  @PostMapping
  public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
    ProductoResponseDTO nuevoProducto = productoService.crearProducto(productoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductoResponseDTO> actualizarProducto(
      @PathVariable Long id,
      @Valid @RequestBody ProductoDTO productoDTO) {
    ProductoResponseDTO productoActualizado = productoService.actualizarProducto(id, productoDTO);
    return ResponseEntity.ok(productoActualizado);
  }

  @PatchMapping("/{id}/stock")
  public ResponseEntity<ProductoResponseDTO> actualizarStock(
      @PathVariable Long id,
      @Valid @RequestBody ActualizarStockDTO stockDTO) {
    ProductoResponseDTO productoActualizado = productoService.actualizarStock(id, stockDTO);
    return ResponseEntity.ok(productoActualizado);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
    productoService.eliminarProducto(id);
    return ResponseEntity.noContent().build();
  }
}
