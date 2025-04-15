package com.flab.mealmate.domain.meetup.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.mealmate.domain.meetup.application.participation.AutoParticipationStrategy;
import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.MeetupMock;
import com.flab.mealmate.domain.meetup.entity.MeetupParticipant;
import com.flab.mealmate.domain.meetup.entity.ParticipationStatus;

@ExtendWith(MockitoExtension.class)
class AutoParticipationStrategyTest {
	private static final Long USER_ID = 123L;

	private Meetup meetup;

	@InjectMocks
	private AutoParticipationStrategy strategy;

	@BeforeEach
	void setUp() {
		meetup = MeetupMock.createAuto(1, 3);
	}
	@Test
	void participateSuccessfully() {
		// when
		strategy.participate(meetup, Optional.empty(), USER_ID);

		// then
		assertThat(meetup.getParticipants()).hasSize(2);
		MeetupParticipant participant = meetup.getParticipants().get(1);

		assertThat(participant.getParticipationStatus()).isEqualTo(ParticipationStatus.APPROVED);
	}

}