package com.flab.mealmate.global.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.flab.mealmate.aop.lock.MySqlLockExecutor;

@Configuration
@Profile("container-test")
public class MySqlTestConfig {

	@Bean
	public MySqlLockExecutor mySqlLockExecutor(DataSource dataSource) {
		return new MySqlLockExecutor(dataSource);
	}

}
