package com.template.microservicetemplate.application.usecase;

import com.template.microservicetemplate.domain.model.entity.Product;
import com.template.microservicetemplate.domain.model.valueobject.Money;
import com.template.microservicetemplate.domain.port.inbound.CreateProductUseCase;
import com.template.microservicetemplate.domain.port.outbound.ProductRepository;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public class CreateProductUseCaseImpl implements CreateProductUseCase {
	private final ProductRepository productRepository;

	public CreateProductUseCaseImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Product create(String name, String description, BigDecimal price, String currencyCode) {
		var currency = Currency.getInstance(currencyCode);
		var money = new Money(price, currency);
		var product = new Product(UUID.randomUUID(), name, description, money, "ACTIVE");
		return productRepository.save(product);
	}
}
