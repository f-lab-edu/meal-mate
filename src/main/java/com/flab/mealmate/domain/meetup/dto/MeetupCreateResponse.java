package com.flab.mealmate.domain.meetup.dto;

import lombok.Getter;

@Getter
public class MeetupCreateResponse {

	private final String id;

	public MeetupCreateResponse(String id) {
		this.id = id;
	}
}
