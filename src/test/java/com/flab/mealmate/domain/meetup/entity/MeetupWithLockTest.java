package com.flab.mealmate.domain.meetup.entity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.mealmate.infra.lock.LockExecutor;

@ExtendWith(MockitoExtension.class)
class MeetupWithLockTest {

	@Mock
	private Meetup meetup;

	@Mock
	private LockExecutor lockExecutor;

	@Test
	void addAutoApprovedParticipantSuccessfully() {
		// given
		Long meetupId = 1L;
		Long userId = 123L;
		MeetupWithLock meetupWithLock = new MeetupWithLock(meetup, lockExecutor);

		given(meetup.getId()).willReturn(meetupId);
		given(lockExecutor.execute(anyString(), anyInt(), anyInt(), any(TimeUnit.class), any()))
			.willAnswer(invocation -> {
				Supplier supplier = invocation.getArgument(4);
				return supplier.get();
			});

		// when
		meetupWithLock.addAutoApprovedParticipant(userId);

		// then
		verify(lockExecutor, times(1))
			.execute(anyString(), anyInt(), anyInt(), any(TimeUnit.class), any());
		verify(meetup, times(1))
			.addAutoApprovedParticipant(userId);

	}

}