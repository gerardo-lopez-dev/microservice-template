package com.template.microservicetemplate.infrastructure.adapter.inbound.rest;

/**
 * Código de ejemplo — eliminar o reemplazar al implementar el dominio real.
 */

import com.template.microservicetemplate.application.dto.request.CreateProductRequest;
import com.template.microservicetemplate.application.dto.response.ProductResponse;
import com.template.microservicetemplate.application.mapper.ProductMapper;
import com.template.microservicetemplate.domain.port.inbound.CreateProductUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
	private final CreateProductUseCase createProductUseCase;

	public ProductController(CreateProductUseCase createProductUseCase) {
		this.createProductUseCase = createProductUseCase;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
		var product = createProductUseCase.create(request.name(), request.description(),
				request.price(), request.currency());
		return ProductMapper.toResponse(product);
	}
}
