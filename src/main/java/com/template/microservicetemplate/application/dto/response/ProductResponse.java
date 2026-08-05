package com.template.microservicetemplate.application.dto.response;

/**
 * Código de ejemplo — eliminar o reemplazar al implementar el dominio real.
 */

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(UUID id, String name, String description, BigDecimal price,
		String currency, String status) {
}
