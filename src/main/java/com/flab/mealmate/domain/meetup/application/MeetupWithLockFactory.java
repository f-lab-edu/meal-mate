package com.flab.mealmate.domain.meetup.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.MeetupWithLock;
import com.flab.mealmate.infra.lock.LockExecutor;

@Component
public class MeetupWithLockFactory {

	private final LockExecutor lockExecutor;

	public MeetupWithLockFactory(@Qualifier("redisLockExecutor")LockExecutor lockExecutor) {
		this.lockExecutor = lockExecutor;
	}

	public MeetupWithLock create(Meetup meetup) {
		return new MeetupWithLock(meetup, lockExecutor);
	}
}
