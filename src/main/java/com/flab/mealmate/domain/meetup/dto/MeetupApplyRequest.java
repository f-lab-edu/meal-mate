package com.flab.mealmate.domain.meetup.dto;

import java.util.Optional;

import org.springframework.util.StringUtils;

public class MeetupApplyRequest {

	private String message;

	public MeetupApplyRequest(String message) {
		this.message = message;
	}

	public Optional<String> getMessage() {
		return Optional.ofNullable(message).filter(StringUtils::hasText);
	}
}
