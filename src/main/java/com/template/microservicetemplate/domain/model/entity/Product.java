package com.template.microservicetemplate.domain.model.entity;

/**
 * Código de ejemplo — eliminar o reemplazar al implementar el dominio real.
 */

import com.template.microservicetemplate.domain.model.valueobject.Money;
import java.util.UUID;

public record Product(UUID id, String name, String description, Money price, String status) {
	public Product {
		if ( name == null || name.isBlank() )
			throw new IllegalArgumentException("name must not be blank");
		if ( price == null )
			throw new IllegalArgumentException("price must not be null");
	}

	public Product withId(UUID id) {
		return new Product(id, name, description, price, status);
	}
}
