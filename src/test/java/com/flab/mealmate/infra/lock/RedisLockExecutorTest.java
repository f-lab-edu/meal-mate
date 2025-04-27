package com.flab.mealmate.infra.lock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flab.mealmate.global.config.AbstractRedisTestContainer;
import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.ErrorCode;
import com.flab.mealmate.infra.lock.RedisLockExecutor;

public class RedisLockExecutorTest extends AbstractRedisTestContainer {

	@Autowired
	private RedisLockExecutor lockExecutor;

	@Test
	void lockSuccessfully() {
		String result = lockExecutor.execute(
			"LOCK:meetup-123",
			5,
			5,
			TimeUnit.SECONDS,
			() -> "락성공!"
		);
		assertThat(result).isEqualTo("락성공!");
	}

	@Test
	void throwsExceptionWhenLockIsAlreadyHeld() throws InterruptedException {
		String lockName = "LOCK:meetup-123";
		CountDownLatch lockAcquired   = new CountDownLatch(1);
		CountDownLatch releaseSignal  = new CountDownLatch(1);

		Thread thread1 = new Thread(() -> {
			lockExecutor.execute(
				lockName,
				5,
				5,
				TimeUnit.SECONDS,
				() -> {
					lockAcquired.countDown();         // 락 획득 알림
					awaitQuietly(releaseSignal);     // 해제 신호 전까지 대기
					return null;
				}
			);
		});
		thread1.start();
		lockAcquired.await(); // thread1이 락을 잡을 때까지 대기

		// 이미 락이 점유되었으므로 예외 발생
		assertThatThrownBy(() ->
			lockExecutor.execute(
				lockName,
				5,
				5,
				TimeUnit.SECONDS,
				() -> "실패해야 함"
			)
		)
			.isInstanceOf(BusinessException.class)
			.hasMessageContaining(ErrorCode.ERR_DB.getValue());

		// 해제 신호 보내고 thread1 종료 대기
		releaseSignal.countDown();
		thread1.join();
	}

	private void awaitQuietly(CountDownLatch latch) {
		try {
			latch.await();
		} catch (InterruptedException ignored) { }
	}
}
