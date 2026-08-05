package com.template.microservicetemplate.infrastructure.adapter.outbound.persistence;

/**
 * Código de ejemplo — eliminar o reemplazar al implementar el dominio real.
 */

import com.template.microservicetemplate.domain.model.entity.Product;
import com.template.microservicetemplate.domain.model.valueobject.Money;
import com.template.microservicetemplate.domain.port.outbound.ProductRepository;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

public class SpringProductRepository implements ProductRepository {
	private final ProductJpaRepository springRepo;

	public SpringProductRepository(ProductJpaRepository springRepo) {
		this.springRepo = springRepo;
	}

	@Override
	public Product save(Product product) {
		var entity = new ProductJpaEntity(product.id(), product.name(), product.description(),
				product.price().amount(), product.price().currency().getCurrencyCode(),
				product.status());
		springRepo.save(entity);
		return product;
	}

	@Override
	public Optional<Product> findById(UUID id) {
		return springRepo.findById(id).map(this::toDomain);
	}

	private Product toDomain(ProductJpaEntity entity) {
		var money = new Money(entity.getPrice(), Currency.getInstance(entity.getCurrency()));
		return new Product(entity.getId(), entity.getName(), entity.getDescription(), money,
				entity.getStatus());
	}
}
