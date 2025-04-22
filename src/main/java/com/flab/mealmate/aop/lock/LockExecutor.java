package com.flab.mealmate.aop.lock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface LockExecutor {
	<T> T execute(String key, int waitTime, int leaseTime, TimeUnit timeUnit, Supplier<T> action);

}
