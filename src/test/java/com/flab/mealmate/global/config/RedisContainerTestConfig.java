package com.flab.mealmate.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.flab.mealmate.infra.lock.RedisLockExecutor;

@Configuration
@Profile("container-test")
public class RedisContainerTestConfig {

	@Value("${spring.data.redis.host}")
	private String redisHost;

	@Value("${spring.data.redis.port}")
	private Integer redisPort;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		String address = redisHost.startsWith("redis://")
			? redisHost + ":" + redisPort
			: "redis://" + redisHost + ":" + redisPort;

		config.useSingleServer()
			.setAddress(address);

		return Redisson.create(config);
	}

	@Bean
	public RedisLockExecutor redisLockExecutor(RedissonClient client) {
		return new RedisLockExecutor(client);
	}

}