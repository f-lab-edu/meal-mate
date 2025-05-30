package com.flab.mealmate.global.config;

import org.mockito.Mockito;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class RedisMockConfig {

	@Bean
	public RedissonClient redissonClient() {
		return Mockito.mock(RedissonClient.class);
	}
}