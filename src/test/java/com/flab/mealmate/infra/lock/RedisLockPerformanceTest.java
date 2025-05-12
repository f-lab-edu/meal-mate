package com.flab.mealmate.infra.lock;

import static com.flab.mealmate.infra.lock.LockPerformanceTester.runConcurrentTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flab.mealmate.global.config.AbstractRedisTestContainer;

public class RedisLockPerformanceTest extends AbstractRedisTestContainer {

	@Autowired
	private RedisLockExecutor redisLockExecutor;

	@Test
	@Disabled
	void testRedisLockPerformance() throws Exception {
		runConcurrentTest(redisLockExecutor, "Redis");
	}
}
