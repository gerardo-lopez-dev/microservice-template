package com.template.microservicetemplate.infrastructure.config;

import com.template.microservicetemplate.application.usecase.CreateProductUseCaseImpl;
import com.template.microservicetemplate.domain.port.inbound.CreateProductUseCase;
import com.template.microservicetemplate.domain.port.outbound.ProductRepository;
import com.template.microservicetemplate.infrastructure.adapter.outbound.persistence.ProductJpaRepository;
import com.template.microservicetemplate.infrastructure.adapter.outbound.persistence.SpringProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
	@Bean
	ProductRepository productRepository(ProductJpaRepository springRepo) {
		return new SpringProductRepository(springRepo);
	}

	@Bean
	CreateProductUseCase createProductUseCase(ProductRepository productRepository) {
		return new CreateProductUseCaseImpl(productRepository);
	}
}
