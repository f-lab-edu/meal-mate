package com.flab.mealmate.aop.lock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flab.mealmate.global.config.AbstractMySqlTestContainer;
import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.ErrorCode;

class MySqlLockExecutorTest extends AbstractMySqlTestContainer {

	@Autowired
	MySqlLockExecutor lockExecutor;

	@Test
	void lockSuccessfully(){
		String result = lockExecutor.execute("LOCK:meetup-123", 5, 5, TimeUnit.SECONDS, () -> "락성공!");
		assertThat(result).isEqualTo("락성공!");
	}

	@Test
	void throwsExceptionWhenLockIsAlreadyHeld() throws InterruptedException {
		String lockName = "LOCK:meetup-123";
		CountDownLatch lockAcquired = new CountDownLatch(1); // thread1이 락을 획득했음을 알리는 신호
		CountDownLatch releaseSignal = new CountDownLatch(1); // thread1이 락을 해제할 수 있게 해주는 신호

		Thread thread1 = new Thread(() -> {
			lockExecutor.execute(
				lockName,
				5,
				5,
				TimeUnit.SECONDS,
				() -> {
					lockAcquired.countDown(); // 락을 획득했음을 main thread에 알림
					awaitQuietly(releaseSignal); // main thread가 해제 신호를 줄 때까지 대기 → 락 점유 유지
					return null;
				});
		});
		thread1.start(); // thread 1 시작 -> thread 1이 먼저 lock 점유
		lockAcquired.await(); // thread1이 락을 잡을 때까지 main thread는 대기

		// 이미 thread 1에서 lock을 점유했기 때문에 예외가 발생해야 함
		assertThatThrownBy(() ->
			lockExecutor.execute(lockName, 5, 5, TimeUnit.SECONDS, () -> "실패해야 함")
		).isInstanceOf(BusinessException.class)
			.hasMessageContaining(ErrorCode.ERR_DB.getValue());

		releaseSignal.countDown(); // thread 1이 lock 반납하도록 신호
		thread1.join(); // thread1 종료까지 대기
	}

	private void awaitQuietly(CountDownLatch latch) {
		try {
			latch.await();
		} catch (InterruptedException ignored) {
		}
	}

}