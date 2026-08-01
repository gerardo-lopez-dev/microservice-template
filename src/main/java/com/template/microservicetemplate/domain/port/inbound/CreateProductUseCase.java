package com.template.microservicetemplate.domain.port.inbound;

import com.template.microservicetemplate.domain.model.entity.Product;

public interface CreateProductUseCase {
	Product create(String name, String description, java.math.BigDecimal price, String currency);
}
