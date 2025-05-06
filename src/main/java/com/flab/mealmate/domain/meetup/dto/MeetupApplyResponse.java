package com.flab.mealmate.domain.meetup.dto;

import lombok.Getter;

@Getter
public class MeetupApplyResponse {

	private final String meetupId;

	public MeetupApplyResponse(String meetupId) {
		this.meetupId = meetupId;
	}
}
