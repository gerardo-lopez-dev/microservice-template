package com.template.microservicetemplate.infrastructure.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;

@ExtendWith(MockitoExtension.class)
class DbHealthIndicatorTest {

	@Mock
	private DataSource dataSource;

	@Mock
	private Connection connection;

	@Mock
	private Statement statement;

	private DbHealthIndicator indicator;

	@BeforeEach
	void setUp() {
		indicator = new DbHealthIndicator(dataSource);
	}

	@Test
	void returnsUpWhenDatabaseReachable() throws SQLException {
		when(dataSource.getConnection()).thenReturn(connection);
		when(connection.createStatement()).thenReturn(statement);
		when(statement.execute("SELECT 1")).thenReturn(true);

		Health health = indicator.health();
		assertEquals(Status.UP, health.getStatus());
	}

	@Test
	void returnsDownWhenDatabaseUnreachable() throws SQLException {
		when(dataSource.getConnection()).thenThrow(new SQLException("connection refused"));

		Health health = indicator.health();
		assertEquals(Status.DOWN, health.getStatus());
		assertEquals("unreachable", health.getDetails().get("database"));
	}
}
