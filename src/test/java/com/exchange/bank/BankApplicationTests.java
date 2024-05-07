package com.exchange.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class BankApplicationTests {

	@SuppressWarnings("resource")
	@ServiceConnection
	@Container
	public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("bank_exchange")
			.withUsername("postgres")
			.withPassword("postgres");

	static {
		postgres.start();
	}

	@Test
	void contextLoads() {
	}

}
