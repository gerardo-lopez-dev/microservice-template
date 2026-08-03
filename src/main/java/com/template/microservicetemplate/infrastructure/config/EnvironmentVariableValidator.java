package com.template.microservicetemplate.infrastructure.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class EnvironmentVariableValidator implements ApplicationRunner {

	private static final List<String> REQUIRED_VARIABLES = List.of("DB_URL", "DB_USERNAME", "DB_PASSWORD");

	private final Environment environment;

	public EnvironmentVariableValidator(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void run(ApplicationArguments args) {
		validateRequiredVariables();
	}

	void validateRequiredVariables() {
		List<String> missing = new ArrayList<>();
		for (String variable : REQUIRED_VARIABLES) {
			String value = environment.getProperty(variable);
			if (value == null || value.isBlank()) {
				missing.add(variable);
			}
		}
		if (!missing.isEmpty()) {
			throw new IllegalStateException(
					"Missing required environment variables for prod profile: " + String.join(", ", missing));
		}
	}

}
