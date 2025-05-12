package com.flab.mealmate.infra.lock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

public class LockExecutionContext {

	private final LockExecutor lockExecutor;
	private String key;
	private int waitTime = 5;
	private int leaseTime = 3;
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	private LockExecutionContext(LockExecutor lockExecutor) {
		this.lockExecutor = lockExecutor;
	}

	public static LockExecutionContext with(LockExecutor lockExecutor) {
		return new LockExecutionContext(lockExecutor);
	}

	public LockExecutionContext buildKey(String[] keys, Object[] values) {
		this.key = CompositeKeyGenerator.buildKey(keys, values);
		return this;
	}

	public boolean executeWithTask(Runnable task) {
		return executeInternal(() -> {
			task.run();
			return true;
		});
	}

	private <T> T executeInternal(Supplier<T> task) {
		if (key == null) {
			throw new CustomIllegalArgumentException(ErrorCode.ERR_LOCK_KEY_003);
		}
		return lockExecutor.execute(key, waitTime, leaseTime, timeUnit, task);
	}
}
