package com.flab.mealmate.domain.meetup.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class MeetupSearchResponse {
	private final Long totalCount;
	private final String  cursorId;
	private final List<MeetupSummary> meetups;

	public MeetupSearchResponse(Long totalCount, String cursorId, List<MeetupSummary> meetups) {
		this.totalCount = totalCount;
		this.cursorId = cursorId;
		this.meetups = meetups;
	}
}
