package com.flab.mealmate.domain.meetup.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.flab.mealmate.domain.model.User;
import com.flab.mealmate.global.config.AbstractRedisTestContainer;
import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.infra.lock.RedisLockExecutor;

public class MeetupWithLockConcurrencyTest extends AbstractRedisTestContainer {

	@Autowired
	private RedisLockExecutor executor;

	private Meetup meetup;

	@BeforeEach
	void setUp() {
		meetup = MeetupMock.createAuto(1, 2); // 최대 2명
		Long hostId = 1L;
		ReflectionTestUtils.setField(meetup, "createdBy", new User(hostId, String.valueOf(hostId)));
	}

	@Test
	void NotExceedsMaxParticipantsWithLocking() throws InterruptedException {
		int threadCount = 10;
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(threadCount);
		AtomicInteger exception = new AtomicInteger();

		MeetupWithLock meetupWithLock = new MeetupWithLock(meetup, executor);

		for (int i = 0; i < threadCount; i++) {
			final long userId = 1000L + i;

			new Thread(() -> {
				awaitQuietly(startLatch);
				try {
					meetupWithLock.addAutoApprovedParticipant(userId);
					var participant = meetup.getParticipants().get(meetup.getParticipants().size() - 1);
					ReflectionTestUtils.setField(participant, "createdBy", new User(userId, String.valueOf(userId)));
				} catch (BusinessException ignored) {
					exception.getAndIncrement();
				} finally {
					doneLatch.countDown();
				}
			}).start();
		}

		startLatch.countDown(); // 모든 스레드 동시에 시작
		doneLatch.await();      // 모두 완료될 때까지 대기

		assertThat(meetup.getParticipants().size()).isLessThanOrEqualTo(2);
		assertThat(exception.get()).isEqualTo(9);
	}

	private void awaitQuietly(CountDownLatch latch) {
		try {
			latch.await();
		} catch (InterruptedException ignored) {
		}
	}
}
