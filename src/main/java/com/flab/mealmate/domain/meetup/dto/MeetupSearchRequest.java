package com.flab.mealmate.domain.meetup.dto;

import static com.flab.mealmate.domain.meetup.entity.ProgressStatus.SCHEDULED;

import com.flab.mealmate.domain.meetup.entity.ProgressStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MeetupSearchRequest {

	private final ProgressStatus progressStatus = SCHEDULED;
	private final String keyword;
	@NotNull @Max(value = 100)
	private final int size;
	private final Long cursorId;

	public MeetupSearchRequest(String keyword, int size, Long cursorId) {
		this.keyword = keyword;
		this.size = size;
		this.cursorId = cursorId;
	}
}
