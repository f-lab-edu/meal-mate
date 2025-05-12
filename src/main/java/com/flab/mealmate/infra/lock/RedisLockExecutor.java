package com.flab.mealmate.infra.lock;

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

		try {
			lock.lock(leaseTime, timeUnit);
			log.debug("Redis 락 획득 성공: {}", fullKey);
			return action.get();
		} catch (Exception e) {
			log.error("Redis 락 처리 중 예외 발생: {}", fullKey, e);
			throw new BusinessException(ErrorCode.ERR_DB, new String[]{fullKey});
		} finally {
			try {
				if (lock.isHeldByCurrentThread()) {
					lock.unlock();
					log.debug("Redis 락 해제 성공: {}", fullKey);
				}
			} catch (IllegalMonitorStateException e) {
				log.error("Redis 락 해제 실패: {}", fullKey, e);
				throw new BusinessException(ErrorCode.ERR_DB, new String[]{fullKey});
			}
		}
	}
}

