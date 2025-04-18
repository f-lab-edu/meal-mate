package com.flab.mealmate.aop.redislock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

	/**
	 * 락 이름
	 */
	String key();

	/**
	 * 락 유효 시간 단위 : 초(sec)
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * 락 획득을 위해 기다리는 시간
	 * default : 5s
	 */
	long waitTime() default 5L;

	/**
	 * 락 획득 유지 시간
	 * 락을 획득한 후 leaseTime이 끝나면 락 해제
	 * waitTime보다 작은값
	 * */
	long leaseTime() default  3L;
}
