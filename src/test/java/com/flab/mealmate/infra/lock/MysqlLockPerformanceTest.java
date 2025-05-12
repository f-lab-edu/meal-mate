package com.flab.mealmate.infra.lock;

import static com.flab.mealmate.infra.lock.LockPerformanceTester.runConcurrentTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flab.mealmate.global.config.AbstractMySqlTestContainer;

public class MysqlLockPerformanceTest extends AbstractMySqlTestContainer {

	@Autowired
	private MySqlLockExecutor mySqlLockExecutor;

	@Test
	@Disabled
	void testMysqlLockPerformance() throws Exception {
		runConcurrentTest(mySqlLockExecutor, "Mysql");
	}
}
