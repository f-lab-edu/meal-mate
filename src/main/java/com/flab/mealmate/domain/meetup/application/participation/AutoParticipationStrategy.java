package com.flab.mealmate.domain.meetup.application.participation;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutoParticipationStrategy implements ParticipationStrategy {

	@Override
	public ParticipationType getType() {
		return ParticipationType.AUTO;
	}

	@Override
	public void participate(Meetup meetup, Optional<String> applicationMessage, Long userId) {
		// AUTO 타입은 자동으로 신청되어 applicationMessage를 사용하지 않음
		meetup.addAutoApprovedParticipant(userId);
	}

}
