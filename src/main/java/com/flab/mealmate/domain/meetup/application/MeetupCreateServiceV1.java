package com.flab.mealmate.domain.meetup.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.mealmate.domain.meetup.dao.MeetupRepository;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateResponse;
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

	@Override
	public MeetupCreateResponse create(MeetupCreateRequest request) {
		var meetUp = meetupCreateMapper.toEntity(request, timeProvider.now(), meetupTimePolicy);
		meetupRepository.save(meetUp);

		return meetupCreateMapper.toResponse(meetUp);
	}


}
