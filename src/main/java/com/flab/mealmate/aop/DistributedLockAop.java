package com.flab.mealmate.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 특정 비즈니스 로직이나 도메인에 한정되지 않고, 범용적으로 사용할 수 있도록 AOP로 분리
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

	private static final String REDISSON_LOCK_PREFIX = "LOCK:";
	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(com.flab.mealmate.aop.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

		String key = REDISSON_LOCK_PREFIX + RedisKeyResolver.buildKey(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
		// 락의 이름으로 RLock 인스턴스를 가져온다.
		RLock rLock = redissonClient.getLock(key);

		try {
			// waitTime까지 Lock 획득을 시도한다.
			boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
			if (!available) {
				return false;
			}
			// DistributedLock 어노테이션이 선언된 메서드를 별도의 트랜잭션으로 실행한다.(aopForTransaction 정의)
			return aopForTransaction.proceed(joinPoint);
		} catch (InterruptedException e) {
			throw new InterruptedException();
		} finally {
			try {
				//종료시 무조건 락을 해제한다.
				rLock.unlock();
			} catch (IllegalMonitorStateException e) {
				log.info("Redisson Lock Already UnLock {} {}",
					method.getName(), key
				);
			}
		}
	}

}