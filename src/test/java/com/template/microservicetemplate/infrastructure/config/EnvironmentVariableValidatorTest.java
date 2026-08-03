package com.template.microservicetemplate.infrastructure.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class EnvironmentVariableValidatorTest {

	@Mock
	private Environment environment;

	private EnvironmentVariableValidator validator;

	@BeforeEach
	void setUp() {
		validator = new EnvironmentVariableValidator(environment);
	}

	@Test
	void failsWhenRequiredVariableMissing() {
		when(environment.getProperty("DB_URL")).thenReturn(null);
		when(environment.getProperty("DB_USERNAME")).thenReturn("postgres");
		when(environment.getProperty("DB_PASSWORD")).thenReturn("secret");

		assertThrows(IllegalStateException.class, validator::validateRequiredVariables);
	}

	@Test
	void failsWhenRequiredVariableBlank() {
		when(environment.getProperty("DB_URL")).thenReturn("jdbc:postgresql://localhost:5432/prod_db");
		when(environment.getProperty("DB_USERNAME")).thenReturn("   ");
		when(environment.getProperty("DB_PASSWORD")).thenReturn("secret");

		assertThrows(IllegalStateException.class, validator::validateRequiredVariables);
	}

	@Test
	void passesWhenAllRequiredVariablesPresent() {
		when(environment.getProperty("DB_URL")).thenReturn("jdbc:postgresql://localhost:5432/prod_db");
		when(environment.getProperty("DB_USERNAME")).thenReturn("postgres");
		when(environment.getProperty("DB_PASSWORD")).thenReturn("secret");

		assertDoesNotThrow(validator::validateRequiredVariables);
	}

}
