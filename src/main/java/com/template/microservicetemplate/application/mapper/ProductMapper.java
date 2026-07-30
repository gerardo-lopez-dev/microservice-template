package com.template.microservicetemplate.application.mapper;

import com.template.microservicetemplate.application.dto.response.ProductResponse;
import com.template.microservicetemplate.domain.model.entity.Product;

public class ProductMapper {
	public static ProductResponse toResponse(Product product) {
		return new ProductResponse(product.id(), product.name(), product.description(),
				product.price().amount(), product.price().currency().getCurrencyCode(),
				product.status());
	}
}
