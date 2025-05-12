package com.flab.mealmate.domain.meetup.mapper;

import com.flab.mealmate.domain.meetup.dto.MeetupApplyResponse;
import com.flab.mealmate.domain.meetup.entity.Meetup;

public class MeetupApplyMapper {

	private MeetupApplyMapper() {
		throw new UnsupportedOperationException();
	}

	public static MeetupApplyResponse toResponse(Meetup meetup) {
		return new MeetupApplyResponse(String.valueOf(meetup.getId()));
	}
}
