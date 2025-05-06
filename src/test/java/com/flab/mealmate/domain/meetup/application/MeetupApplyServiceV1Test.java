package com.flab.mealmate.domain.meetup.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.mealmate.domain.meetup.application.participation.ParticipationStrategy;
import com.flab.mealmate.domain.meetup.dao.MeetupRepository;
import com.flab.mealmate.domain.meetup.dto.MeetupApplyRequest;
import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.MeetupMock;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.global.error.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class MeetupApplyServiceV1Test {

	@Mock
	private MeetupRepository meetupRepository;

	@Mock
	private ParticipationStrategy autoParticipationStrategy;
	@Mock
	private ParticipationStrategy approvalParticipationStrategy;

	private MeetupApplyServiceV1 service;

	private MeetupApplyRequest applyRequest;

	@BeforeEach
	void setUp() {
		given(autoParticipationStrategy.getType()).willReturn(ParticipationType.AUTO);
		given(approvalParticipationStrategy.getType()).willReturn(ParticipationType.APPROVAL);

		service = new MeetupApplyServiceV1(
			List.of(autoParticipationStrategy, approvalParticipationStrategy),
			meetupRepository
		);

		applyRequest = new MeetupApplyRequest("신청합니다.");
	}


	@Test
	void shouldInvokeParticipationStrategy() {
		// given
		Long meetupId = 1L;
		Long userId = 42L;
		Meetup meetup = MeetupMock.createAuto(3, 5);

		given(meetupRepository.findById(anyLong())).willReturn(Optional.of(meetup));
		service.apply(meetupId, applyRequest, userId);

		verify(autoParticipationStrategy, times(1))
			.participate(
				any(Meetup.class),
				any(Optional.class),
				anyLong()
			);
	}
	@Test
	void throwsExceptionWhenMeetupNotFound() {
		Long meetupId = 999L;
		given(meetupRepository.findById(meetupId)).willReturn(Optional.empty());

		// when & then
		assertThrows(BusinessException.class, () -> {
			service.apply(meetupId, applyRequest, 1L);
		});
	}

}