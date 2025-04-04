package com.flab.mealmate.domain.meetup.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.mealmate.domain.meetup.dao.CustomMeetupRepository;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResponse;
import com.flab.mealmate.domain.meetup.mapper.MeetupSearchMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetupSearchServiceV1 implements MeetupSearchService {

	private final MeetupSearchMapper searchMapper;
	private final CustomMeetupRepository meetupRepository;

	@Override
	public MeetupSearchResponse search(MeetupSearchRequest request) {
		var criteria = searchMapper.toCriteria(request);
		var result = meetupRepository.page(criteria);

		return searchMapper.toResponse(result);
	}
}
