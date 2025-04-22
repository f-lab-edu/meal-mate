package com.flab.mealmate.global.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

@SpringJUnitConfig
@ImportAutoConfiguration(DataSourceAutoConfiguration.class)
@ActiveProfiles("container-test")
public abstract class AbstractMySqlTestContainer {

	private static final String MYSQL_IMAGE = "mysql:8.2";

	private static final JdbcDatabaseContainer MYSQL;

	static {
		MYSQL = new MySQLContainer(MYSQL_IMAGE);
		MYSQL.start();
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
		registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
		registry.add("spring.datasource.username", MYSQL::getUsername);
		registry.add("spring.datasource.password", MYSQL::getPassword);
	}
}
