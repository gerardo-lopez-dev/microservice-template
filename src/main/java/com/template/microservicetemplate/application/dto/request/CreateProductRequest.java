package com.template.microservicetemplate.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record CreateProductRequest(@NotBlank String name, String description,
		@PositiveOrZero BigDecimal price, @NotBlank String currency) {
}
