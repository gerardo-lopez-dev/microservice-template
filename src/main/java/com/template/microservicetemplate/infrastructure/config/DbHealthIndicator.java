package com.template.microservicetemplate.infrastructure.config;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DbHealthIndicator implements HealthIndicator {

	private final DataSource dataSource;

	public DbHealthIndicator(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Health health() {
		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.execute("SELECT 1");
			return Health.up().withDetail("database", "reachable").build();
		}
		catch ( Exception e ) {
			return Health.down().withDetail("database", "unreachable")
					.withDetail("error", e.getMessage()).build();
		}
	}
}
