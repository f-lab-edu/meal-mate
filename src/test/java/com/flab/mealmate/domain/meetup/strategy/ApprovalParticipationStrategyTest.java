package com.flab.mealmate.domain.meetup.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.mealmate.domain.meetup.application.participation.ApprovalParticipationStrategy;
import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.MeetupMock;
import com.flab.mealmate.domain.meetup.entity.ParticipationStatus;
import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class ApprovalParticipationStrategyTest {
	@InjectMocks
	private ApprovalParticipationStrategy strategy;

	private Meetup meetup;

	private final Long USER_ID = 123L;

	@BeforeEach
	void setUp() {
		meetup = MeetupMock.createAuto(1, 3);
	}

	@Test
	void participateSuccessfully() {
		//given
		String message = "참여하고 싶어요.";
		// when
		strategy.participate(meetup, Optional.of(message), USER_ID);

		// then
		assertThat(meetup.getParticipants()).hasSize(2);
		assertThat(meetup.getParticipants().get(1).getParticipationStatus()).isEqualTo(ParticipationStatus.PENDING);
	}

	@Test
	void throwsExceptionWhenMessageIsNull() {
		// when & then
		assertThatThrownBy(() -> strategy.participate(meetup, Optional.empty(), USER_ID))
			.isInstanceOf(CustomIllegalArgumentException.class)
			.satisfies(e -> {
				CustomIllegalArgumentException ex = (CustomIllegalArgumentException)e;
				assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ERR_MEETUP_PARTICIPANT_001);
			});
	}

}