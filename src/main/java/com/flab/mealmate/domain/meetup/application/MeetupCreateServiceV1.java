package com.flab.mealmate.domain.meetup.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.mealmate.domain.meetup.dao.MeetupParticipantRepository;
import com.flab.mealmate.domain.meetup.dao.MeetupRepository;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateResponse;
import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.MeetupParticipant;
import com.flab.mealmate.domain.meetup.mapper.MeetupCreateMapper;
import com.flab.mealmate.domain.meetup.policy.MeetupTimePolicy;
import com.flab.mealmate.global.common.TimeProvider;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetupCreateServiceV1 implements MeetupCreateService {

	private final MeetupRepository meetupRepository;

	private final MeetupCreateMapper meetupCreateMapper;

	private final MeetupTimePolicy meetupTimePolicy;

	private final TimeProvider timeProvider;

	private final MeetupParticipantRepository meetupParticipantRepository;

	@Override
	public MeetupCreateResponse create(MeetupCreateRequest request) {
		var meetUp = meetupCreateMapper.toEntity(request, timeProvider.now(), meetupTimePolicy);

		meetupRepository.save(meetUp);
		addHostAsParticipant(meetUp);

		return meetupCreateMapper.toResponse(meetUp);
	}

	// 호스트 자동 추가 메서드
	private void addHostAsParticipant(Meetup meetup) {
		meetupParticipantRepository.save(MeetupParticipant.createHostParticipant(meetup));
	}

}
