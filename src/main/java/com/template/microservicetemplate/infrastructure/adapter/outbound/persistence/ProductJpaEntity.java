package com.template.microservicetemplate.infrastructure.adapter.outbound.persistence;

/**
 * Código de ejemplo — eliminar o reemplazar al implementar el dominio real.
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductJpaEntity {
	@Id
	private UUID id;
	@Column(nullable = false)
	private String name;
	private String description;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;
	@Column(nullable = false, length = 3)
	private String currency;
	@Column(nullable = false)
	private String status;

	protected ProductJpaEntity() {
	}

	public ProductJpaEntity(UUID id, String name, String description, BigDecimal price,
			String currency, String status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.currency = currency;
		this.status = status;
	}

	public UUID getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public String getCurrency() {
		return currency;
	}
	public String getStatus() {
		return status;
	}
}
