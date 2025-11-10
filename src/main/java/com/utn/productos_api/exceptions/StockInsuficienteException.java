package com.utn.productos_api.exceptions;

public class StockInsuficienteException extends ExceptionManager {
  public StockInsuficienteException(String message) {
    super(message);
  }
}
