package com.flab.mealmate.global.config;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringJUnitConfig
@ActiveProfiles("container-test")
@Testcontainers
public abstract class AbstractRedisTestContainer {

	private static final String REDIS_IMAGE = "redis:7.0.8-alpine";

	private static final String REDIS_PREFIX = "redis://";

	private static final int REDIS_PORT = 6379;

	private static final GenericContainer<?> REDIS;

	static {
		REDIS = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
			.withExposedPorts(REDIS_PORT);
		REDIS.start();
	}

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		String addressWithPrefix = REDIS_PREFIX + REDIS.getHost();
		registry.add("spring.data.redis.host",() -> addressWithPrefix);
		registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(REDIS_PORT));
	}

}
