package com.flab.mealmate.domain.meetup.entity;

import com.flab.mealmate.infra.lock.LockExecutionContext;
import com.flab.mealmate.infra.lock.LockExecutor;

public class MeetupWithLock {

	private final Meetup meetup;
	private final LockExecutor lockExecutor;

	public MeetupWithLock(Meetup meetup, LockExecutor lockExecutor) {
		this.meetup = meetup;
		this.lockExecutor = lockExecutor;
	}

	public void addAutoApprovedParticipant(Long userId) {
		LockExecutionContext.with(this.lockExecutor)
			.buildKey(new String[] {"meetup"}, new Object[] {meetup.getId()})
			.executeWithTask(() -> meetup.addAutoApprovedParticipant(userId));
	}

}