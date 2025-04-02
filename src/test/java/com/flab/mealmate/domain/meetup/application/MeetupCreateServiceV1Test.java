package com.flab.mealmate.domain.meetup.application;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.mealmate.domain.meetup.dao.MeetupParticipantRepository;
import com.flab.mealmate.domain.meetup.dao.MeetupRepository;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateResponse;
import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.MeetupParticipant;
import com.flab.mealmate.domain.meetup.entity.MeetupSchedule;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.domain.meetup.mapper.MeetupCreateMapper;
import com.flab.mealmate.domain.meetup.policy.MeetupTimePolicy;
import com.flab.mealmate.global.common.TimeProvider;

@ExtendWith(MockitoExtension.class)
class MeetupCreateServiceV1Test {

	@Mock
	private MeetupRepository meetupRepository;

	@Mock
	private MeetupCreateMapper meetupCreateMapper;

	@Mock
	private MeetupTimePolicy meetupTimePolicy;

	@Mock
	private TimeProvider timeProvider;

	@Mock
	private MeetupParticipantRepository meetupParticipantRepository;

	@InjectMocks
	private MeetupCreateServiceV1 meetupCreateService;

	private MeetupCreateRequest request;

	@BeforeEach
	void setUp() {
		var now = LocalDateTime.of(2026, 1, 1, 12, 0, 0, 0);
		given(timeProvider.now()).willReturn(now);

		request = new MeetupCreateRequest(
			"신촌에서 같이 밥먹을 사람",
			"샤브샤브 먹고싶어요.",
			ParticipationType.AUTO,
			timeProvider.now(),
			3,
			5
		);
	}

	@Test
	void create() {
		var schedule = MeetupSchedule.create(request.getStartDateTime(),request.getStartDateTime(), meetupTimePolicy);
		var meetup = new Meetup("신촌에서 같이 밥먹을 사람", "샤브샤브 먹고싶어요.", schedule, ParticipationType.AUTO, 3, 5);
		var response = new MeetupCreateResponse("1");

		given(meetupCreateMapper.toEntity(
			any(MeetupCreateRequest.class),
			any(LocalDateTime.class),
			any(MeetupTimePolicy.class)
		)).willReturn(meetup);
		given(meetupRepository.save(any(Meetup.class))).willReturn(meetup);
		given(meetupCreateMapper.toResponse(any(Meetup.class))).willReturn(response);

		MeetupCreateResponse result = meetupCreateService.create(request);

		assertNotNull(result);
		assertEquals("1", result.getId());

		verify(meetupCreateMapper, times(1)).toEntity(request, timeProvider.now(), meetupTimePolicy);
		verify(meetupRepository, times(1)).save(meetup);
		verify(meetupParticipantRepository, times(1)).save(any(MeetupParticipant.class));
		verify(meetupCreateMapper, times(1)).toResponse(meetup);
	}

}