package com.template.microservicetemplate.domain.model.valueobject;

/**
 * Código de ejemplo — eliminar o reemplazar al implementar el dominio real.
 */

import java.math.BigDecimal;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {
	public Money {
		if ( amount == null || amount.compareTo(BigDecimal.ZERO) < 0 )
			throw new IllegalArgumentException("amount must be non-negative");
		if ( currency == null )
			throw new IllegalArgumentException("currency must not be null");
	}

	public static Money usd(BigDecimal amount) {
		return new Money(amount, Currency.getInstance("USD"));
	}
}
