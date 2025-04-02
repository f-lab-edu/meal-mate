package com.flab.mealmate.global.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 종류에 따른 Datasource 분리를 위한 설정
 */
@Slf4j
@Profile({"prd"})
@Configuration
public class DataSourceConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.read")
	public DataSource readDataSource() {
		return DataSourceBuilder.create()
			.type(com.zaxxer.hikari.HikariDataSource.class)
			.build();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.write")
	public DataSource writeDataSource() {
		return DataSourceBuilder.create()
			.type(com.zaxxer.hikari.HikariDataSource.class)
			.build();
	}


	@Bean
	public DataSource routingDataSource(
		@Qualifier("writeDataSource") DataSource writeDataSource,
		@Qualifier("readDataSource") DataSource readDataSource
	) {
		ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

		Map<Object, Object> dataSourceMap = new HashMap<>();
		dataSourceMap.put("write", writeDataSource);
		dataSourceMap.put("read", readDataSource);
		routingDataSource.setTargetDataSources(dataSourceMap);
		routingDataSource.setDefaultTargetDataSource(writeDataSource);

		return routingDataSource;
	} // routingDataSource()

	@Bean
	@Primary
	public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
		return new LazyConnectionDataSourceProxy(routingDataSource);
	} // dataSource()
}
