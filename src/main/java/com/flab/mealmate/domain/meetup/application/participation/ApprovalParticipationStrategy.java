package com.flab.mealmate.domain.meetup.application.participation;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApprovalParticipationStrategy implements ParticipationStrategy {

	@Override
	public ParticipationType getType() {
		return ParticipationType.APPROVAL;
	}

	@Override
	public void participate(Meetup meetup, Optional<String> applicationMessage, Long userId) {
		applicationMessage.ifPresentOrElse(
			msg -> meetup.addPendingParticipant(msg, userId),
			() -> {
				throw new CustomIllegalArgumentException(ErrorCode.ERR_MEETUP_PARTICIPANT_001);
			}
		);
	}
}
