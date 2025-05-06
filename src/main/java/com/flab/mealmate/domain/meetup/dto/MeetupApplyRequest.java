package com.flab.mealmate.domain.meetup.dto;

import java.util.Optional;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeetupApplyRequest {

	@JsonProperty
	private String message;


	public MeetupApplyRequest(String message) {
		this.message = message;
	}

	@JsonIgnore
	public Optional<String> safeMessage() {
		return Optional.ofNullable(message).filter(StringUtils::hasText);
	}
}
