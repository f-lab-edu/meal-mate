package com.flab.mealmate.aop.lock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockExecutor implements LockExecutor {

	private final RedissonClient redissonClient;

	private final String REDIS_LOCK_PREFIX = "LOCK:";

	@Override
	public <T> T execute(String key, int waitTime, int leaseTime, TimeUnit timeUnit, Supplier<T> action) {
		String fullKey = REDIS_LOCK_PREFIX + key;
		RLock lock = redissonClient.getLock(fullKey);

		boolean acquired = false;

		try {
			acquired = lock.tryLock(waitTime, leaseTime, timeUnit);
			if (!acquired) {
				log.warn("Redis 락 획득 실패: {}", fullKey);
				throw new BusinessException(ErrorCode.ERR_DB, new String[]{fullKey});
			}
			// 락을 획득한 상태에서만 action 수행
			return action.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Redis 락 대기 중 인터럽트 발생: {}", fullKey, e);
			throw new BusinessException(ErrorCode.ERR_DB, new String[]{fullKey});
		} finally {
			if (acquired) { // 락을 획득한 경우에만 해제
				try {
					lock.unlock();
				} catch (IllegalMonitorStateException e) {
					log.error("Redis 락 해제 실패: {}", fullKey, e);
					throw new BusinessException(ErrorCode.ERR_DB, new String[]{fullKey});
				}
			}
		}
	}
}

