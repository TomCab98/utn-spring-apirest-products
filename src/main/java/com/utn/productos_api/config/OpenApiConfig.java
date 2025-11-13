package com.utn.productos_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI productosApiOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl("http://localhost:8080");
    devServer.setDescription("Servidor de desarrollo local");

    Info info = new Info()
        .title("API de Gestión de Productos")
        .version("1.0.0")
        .description("API REST completa para la administración de un catálogo de productos. " +
            "Permite realizar operaciones CRUD sobre productos, gestionar inventario, " +
            "filtrar por categorías y mantener el control del stock disponible.");

    return new OpenAPI()
        .info(info)
        .servers(List.of(devServer));
  }
}