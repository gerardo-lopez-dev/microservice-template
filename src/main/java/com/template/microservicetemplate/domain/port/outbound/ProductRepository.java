package com.template.microservicetemplate.domain.port.outbound;

import com.template.microservicetemplate.domain.model.entity.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
	Product save(Product product);
	Optional<Product> findById(UUID id);
}
