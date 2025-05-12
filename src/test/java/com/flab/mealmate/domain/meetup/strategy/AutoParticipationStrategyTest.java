package com.flab.mealmate.domain.meetup.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.mealmate.domain.meetup.application.MeetupWithLockFactory;
import com.flab.mealmate.domain.meetup.application.participation.AutoParticipationStrategy;
import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.MeetupMock;
import com.flab.mealmate.domain.meetup.entity.MeetupWithLock;
import com.flab.mealmate.infra.lock.LockExecutor;

@ExtendWith(MockitoExtension.class)
class AutoParticipationStrategyTest {
	private static final Long USER_ID = 123L;

	@Mock
	MeetupWithLockFactory meetupWithLockFactory;

	@Mock
	LockExecutor lockExecutor;

	@InjectMocks
	AutoParticipationStrategy strategy;

	Meetup meetup;

	@BeforeEach
	void setUp() {
		meetup = MeetupMock.createAuto(1, 3);
	}

	@Test
	void participateSuccessfully() {
		// given
		var meetupWithLock = new MeetupWithLock(meetup, lockExecutor);
		when(meetupWithLockFactory.create(meetup)).thenReturn(meetupWithLock);
		when(lockExecutor.execute(anyString(), anyInt(), anyInt(), any(TimeUnit.class), any()))
			.thenAnswer(invocation -> {
				Supplier action = invocation.getArgument(4);
				return action.get();
			});

		// when
		strategy.participate(meetup, Optional.empty(), USER_ID);

		// then
		verify(lockExecutor, times(1)).execute(anyString(), anyInt(), anyInt(), any(TimeUnit.class), any());
		assertThat(meetup.getParticipants()).hasSize(2);
	}

}