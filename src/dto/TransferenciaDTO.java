package dto;

import java.math.BigDecimal;

public record TransferenciaDTO(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto) {}
