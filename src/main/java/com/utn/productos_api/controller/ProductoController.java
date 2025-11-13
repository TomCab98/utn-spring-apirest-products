package com.utn.productos_api.controller;

import com.utn.productos_api.dto.ActualizarStockDTO;
import com.utn.productos_api.dto.ProductoDTO;
import com.utn.productos_api.dto.ProductoResponseDTO;
import com.utn.productos_api.model.Categoria;
import com.utn.productos_api.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/api/productos")
@Tag(
    name = "Gestión de Productos",
    description = "API REST para la administración completa del catálogo de productos. " +
        "Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre productos, " +
        "consultar por categorías y gestionar el inventario de stock."
)
public class ProductoController {
  private final ProductoService productoService;

  public ProductoController(ProductoService productoService) {
    this.productoService = productoService;
  }

  @Operation(
      summary = "Listar todos los productos",
      description = "Recupera un listado completo de todos los productos registrados en el sistema. " +
          "La respuesta incluye información detallada de cada producto: ID, nombre, descripción, " +
          "precio, stock disponible y categoría. No requiere parámetros de entrada."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Lista de productos obtenida exitosamente",
          content = @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class)),
              examples = @ExampleObject(
                  name = "Lista de productos",
                  value = "[{\"id\":1,\"nombre\":\"Laptop Dell Inspiron 15\",\"descripcion\":\"Laptop con procesador Intel Core i5\",\"precio\":45999.99,\"stock\":25,\"categoria\":\"ELECTRONICA\"},{\"id\":2,\"nombre\":\"Remera Nike Deportiva\",\"descripcion\":\"Remera deportiva talle M\",\"precio\":8500.50,\"stock\":100,\"categoria\":\"ROPA\"}]"
              )
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor al procesar la solicitud",
          content = @Content
      )
  })
  @GetMapping
  public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
    List<ProductoResponseDTO> productos = productoService.obtenerTodos();
    return ResponseEntity.ok(productos);
  }

  @Operation(
      summary = "Obtener producto por ID",
      description = "Recupera la información completa de un producto específico mediante su identificador único (ID). " +
          "Retorna todos los detalles del producto incluyendo nombre, descripción, precio, stock y categoría."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Producto encontrado y retornado exitosamente",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductoResponseDTO.class),
              examples = @ExampleObject(
                  name = "Producto encontrado",
                  value = "{\"id\":1,\"nombre\":\"Laptop Dell Inspiron 15\",\"descripcion\":\"Laptop con procesador Intel Core i5, 8GB RAM, 256GB SSD\",\"precio\":45999.99,\"stock\":25,\"categoria\":\"ELECTRONICA\"}"
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "No se encontró ningún producto con el ID especificado",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "Producto no encontrado",
                  value = "{\"mensaje\":\"Producto no encontrado con ID: 999\",\"timestamp\":\"2025-11-13T10:30:00\"}"
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "El ID proporcionado no tiene un formato válido",
          content = @Content
      )
  })
  @GetMapping("/{id}")
  public ResponseEntity<ProductoResponseDTO> obtenerPorId(
      @Parameter(
          description = "Identificador único del producto a buscar. Debe ser un número entero positivo.",
          required = true,
          example = "1"
      )
      @PathVariable Long id) {
    ProductoResponseDTO producto = productoService.obtenerPorId(id);
    return ResponseEntity.ok(producto);
  }

  @Operation(
      summary = "Filtrar productos por categoría",
      description = "Obtiene todos los productos que pertenecen a una categoría específica. " +
          "Las categorías disponibles son: ELECTRONICA, ROPA, ALIMENTOS, HOGAR, DEPORTES. " +
          "El nombre de la categoría debe enviarse en mayúsculas y debe coincidir exactamente con los valores del enum."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Lista de productos de la categoría obtenida exitosamente. Puede retornar una lista vacía si no hay productos en esa categoría.",
          content = @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class)),
              examples = @ExampleObject(
                  name = "Productos de categoría ELECTRONICA",
                  value = "[{\"id\":1,\"nombre\":\"Laptop Dell Inspiron 15\",\"descripcion\":\"Laptop con procesador Intel Core i5\",\"precio\":45999.99,\"stock\":25,\"categoria\":\"ELECTRONICA\"},{\"id\":3,\"nombre\":\"Mouse Logitech\",\"descripcion\":\"Mouse inalámbrico\",\"precio\":3500.00,\"stock\":50,\"categoria\":\"ELECTRONICA\"}]"
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "La categoría proporcionada no es válida o no existe en el sistema. Valores permitidos: ELECTRONICA, ROPA, ALIMENTOS, HOGAR, DEPORTES",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "Categoría inválida",
                  value = "{\"mensaje\":\"Categoría inválida: TECNOLOGIA. Valores permitidos: ELECTRONICA, ROPA, ALIMENTOS, HOGAR, DEPORTES\",\"timestamp\":\"2025-11-13T10:30:00\"}"
              )
          )
      )
  })
  @GetMapping("/categoria/{categoria}")
  public ResponseEntity<List<ProductoResponseDTO>> filtrarPorCategoria(
      @Parameter(
          description = "Nombre de la categoría para filtrar productos. Debe ser exactamente uno de los siguientes valores: ELECTRONICA, ROPA, ALIMENTOS, HOGAR, DEPORTES",
          required = true,
          example = "ELECTRONICA",
          schema = @Schema(allowableValues = {"ELECTRONICA", "ROPA", "ALIMENTOS", "HOGAR", "DEPORTES"})
      )
      @PathVariable String categoria) {
    List<ProductoResponseDTO> productos = productoService.obtenerPorCategoria(
        Categoria.valueOf(categoria));
    return ResponseEntity.ok(productos);
  }

  @Operation(
      summary = "Crear nuevo producto",
      description = "Registra un nuevo producto en el sistema con todos sus datos. " +
          "Se deben proporcionar todos los campos obligatorios: nombre (3-100 caracteres), " +
          "precio (mínimo $0.01), stock (mínimo 0) y categoría válida. " +
          "La descripción es opcional pero tiene un límite de 500 caracteres. " +
          "El ID se genera automáticamente por el sistema."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Producto creado exitosamente en el sistema. Retorna el producto completo con el ID asignado.",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductoResponseDTO.class),
              examples = @ExampleObject(
                  name = "Producto creado",
                  value = "{\"id\":10,\"nombre\":\"Laptop Dell Inspiron 15\",\"descripcion\":\"Laptop con procesador Intel Core i5, 8GB RAM, 256GB SSD\",\"precio\":45999.99,\"stock\":25,\"categoria\":\"ELECTRONICA\"}"
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Datos de entrada inválidos o incompletos. La respuesta detalla qué campos tienen errores de validación.",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "Errores de validación",
                  value = "{\"errores\":{\"nombre\":\"El nombre debe tener entre 3 y 100 caracteres\",\"precio\":\"El valor no puede ser inferior a $0,01\",\"stock\":\"El stock no puede ser nulo\"},\"timestamp\":\"2025-11-13T10:30:00\"}"
              )
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "Conflicto: Ya existe un producto con datos únicos similares (por ejemplo, mismo nombre)",
          content = @Content
      )
  })
  @PostMapping
  public ResponseEntity<ProductoResponseDTO> crearProducto(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Datos del producto a crear. Todos los campos marcados como obligatorios deben ser proporcionados.",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductoDTO.class),
              examples = @ExampleObject(
                  name = "Ejemplo de producto nuevo",
                  value = "{\"nombre\":\"Laptop Dell Inspiron 15\",\"descripcion\":\"Laptop con procesador Intel Core i5, 8GB RAM, 256GB SSD\",\"precio\":45999.99,\"stock\":25,\"categoria\":\"ELECTRONICA\"}"
              )
          )
      )
      @Valid @RequestBody ProductoDTO productoDTO) {
    ProductoResponseDTO nuevoProducto = productoService.crearProducto(productoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
  }

  @Operation(
      summary = "Actualizar producto completo",
      description = "Actualiza todos los datos de un producto existente identificado por su ID. " +
          "Esta es una actualización completa (PUT), por lo que se deben proporcionar TODOS los campos " +
          "del producto, incluso aquellos que no cambien. Los campos que no se envíen se establecerán " +
          "con valores por defecto o nulos. El ID del producto no puede ser modificado."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Producto actualizado exitosamente. Retorna el producto con todos los datos actualizados.",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductoResponseDTO.class),
              examples = @ExampleObject(
                  name = "Producto actualizado",
                  value = "{\"id\":1,\"nombre\":\"Laptop Dell Inspiron 15 - Actualizada\",\"descripcion\":\"Laptop con procesador Intel Core i7, 16GB RAM, 512GB SSD\",\"precio\":55999.99,\"stock\":30,\"categoria\":\"ELECTRONICA\"}"
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "No se encontró ningún producto con el ID especificado para actualizar",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "Producto no encontrado",
                  value = "{\"mensaje\":\"Producto no encontrado con ID: 999\",\"timestamp\":\"2025-11-13T10:30:00\"}"
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Datos de entrada inválidos. Revise las validaciones de cada campo en la respuesta.",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "Errores de validación",
                  value = "{\"errores\":{\"precio\":\"El valor no puede ser inferior a $0,01\",\"descripcion\":\"Limite excedido - Caracteres permitidos: 500\"},\"timestamp\":\"2025-11-13T10:30:00\"}"
              )
          )
      )
  })
  @PutMapping("/{id}")
  public ResponseEntity<ProductoResponseDTO> actualizarProducto(
      @Parameter(
          description = "ID del producto a actualizar. Debe existir en el sistema.",
          required = true,
          example = "1"
      )
      @PathVariable Long id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Nuevos datos completos del producto. Todos los campos deben ser proporcionados.",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductoDTO.class),
              examples = @ExampleObject(
                  name = "Datos de actualización",
                  value = "{\"nombre\":\"Laptop Dell Inspiron 15 - Actualizada\",\"descripcion\":\"Laptop con procesador Intel Core i7, 16GB RAM, 512GB SSD\",\"precio\":55999.99,\"stock\":30,\"categoria\":\"ELECTRONICA\"}"
              )
          )
      )
      @Valid @RequestBody ProductoDTO productoDTO) {
    ProductoResponseDTO productoActualizado = productoService.actualizarProducto(id, productoDTO);
    return ResponseEntity.ok(productoActualizado);
  }

  @Operation(
      summary = "Actualizar stock del producto",
      description = "Actualización parcial (PATCH) que permite modificar únicamente la cantidad de stock " +
          "de un producto sin necesidad de enviar todos los demás datos. " +
          "Útil para operaciones de inventario rápidas como registrar ventas, ingresos de mercadería " +
          "o ajustes de inventario. El stock debe ser un valor entero mayor o igual a cero."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Stock actualizado exitosamente. Retorna el producto completo con el stock modificado.",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductoResponseDTO.class),
              examples = @ExampleObject(
                  name = "Stock actualizado",
                  value = "{\"id\":1,\"nombre\":\"Laptop Dell Inspiron 15\",\"descripcion\":\"Laptop con procesador Intel Core i5, 8GB RAM, 256GB SSD\",\"precio\":45999.99,\"stock\":50,\"categoria\":\"ELECTRONICA\"}"
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "No se encontró ningún producto con el ID especificado",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "Producto no encontrado",
                  value = "{\"mensaje\":\"Producto no encontrado con ID: 999\",\"timestamp\":\"2025-11-13T10:30:00\"}"
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Valor de stock inválido. Debe ser un número entero mayor o igual a cero.",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "Stock inválido",
                  value = "{\"errores\":{\"stock\":\"El stock no puede ser inferior a 0\"},\"timestamp\":\"2025-11-13T10:30:00\"}"
              )
          )
      )
  })
  @PatchMapping("/{id}/stock")
  public ResponseEntity<ProductoResponseDTO> actualizarStock(
      @Parameter(
          description = "ID del producto cuyo stock se actualizará",
          required = true,
          example = "1"
      )
      @PathVariable Long id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Nueva cantidad de stock para el producto. Debe ser un valor entero no negativo.",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ActualizarStockDTO.class),
              examples = @ExampleObject(
                  name = "Actualización de stock",
                  value = "{\"stock\":50}"
              )
          )
      )
      @Valid @RequestBody ActualizarStockDTO stockDTO) {
    ProductoResponseDTO productoActualizado = productoService.actualizarStock(id, stockDTO);
    return ResponseEntity.ok(productoActualizado);
  }

  @Operation(
      summary = "Eliminar producto",
      description = "Elimina permanentemente un producto del sistema mediante su ID. " +
          "Esta operación es irreversible y no se puede deshacer. " +
          "Si el producto tiene referencias en otras entidades (como órdenes de compra), " +
          "la eliminación podría fallar con un código 409 (Conflict). " +
          "En caso de éxito, no retorna contenido en el cuerpo de la respuesta."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "Producto eliminado exitosamente. No retorna contenido en el cuerpo de la respuesta.",
          content = @Content
      ),
      @ApiResponse(
          responseCode = "404",
          description = "No se encontró ningún producto con el ID especificado para eliminar",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "Producto no encontrado",
                  value = "{\"mensaje\":\"Producto no encontrado con ID: 999\",\"timestamp\":\"2025-11-13T10:30:00\"}"
              )
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "Conflicto: No se puede eliminar el producto porque tiene referencias en otras entidades del sistema (por ejemplo, órdenes de compra, ventas registradas, etc.)",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "Conflicto de integridad",
                  value = "{\"mensaje\":\"No se puede eliminar el producto con ID: 1 porque tiene órdenes asociadas\",\"timestamp\":\"2025-11-13T10:30:00\"}"
              )
          )
      )
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarProducto(
      @Parameter(
          description = "ID del producto a eliminar del sistema. La eliminación es permanente e irreversible.",
          required = true,
          example = "1"
      )
      @PathVariable Long id) {
    productoService.eliminarProducto(id);
    return ResponseEntity.noContent().build();
  }
}