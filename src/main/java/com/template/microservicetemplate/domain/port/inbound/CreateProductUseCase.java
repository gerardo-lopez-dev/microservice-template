package com.template.microservicetemplate.domain.port.inbound;

/**
 * Código de ejemplo — eliminar o reemplazar al implementar el dominio real.
 */

import com.template.microservicetemplate.domain.model.entity.Product;

public interface CreateProductUseCase {
	Product create(String name, String description, java.math.BigDecimal price, String currency);
}
