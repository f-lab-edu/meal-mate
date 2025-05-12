package com.flab.mealmate.domain.meetup.application.participation;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.flab.mealmate.domain.meetup.application.MeetupWithLockFactory;
import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.MeetupWithLock;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutoParticipationStrategy implements ParticipationStrategy {

	private final MeetupWithLockFactory meetupWithLockFactory;

	@Override
	public ParticipationType getType() {
		return ParticipationType.AUTO;
	}

	@Override
	public void participate(Meetup meetup, Optional<String> applicationMessage, Long userId) {
		MeetupWithLock meetupWithLock = meetupWithLockFactory.create(meetup);
		meetupWithLock.addAutoApprovedParticipant(userId);
	}

}
