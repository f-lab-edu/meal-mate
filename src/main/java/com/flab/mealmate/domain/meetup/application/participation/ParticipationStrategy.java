package com.flab.mealmate.domain.meetup.application.participation;

import java.util.Optional;

import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;

public interface ParticipationStrategy {

	ParticipationType getType();

	void participate(Meetup meetup, Optional<String> applicationMessage, Long userId);

}
