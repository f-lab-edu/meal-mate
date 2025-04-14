package com.flab.mealmate.domain.meetup.dto;

import static com.flab.mealmate.domain.meetup.entity.ProgressStatus.*;

import org.springframework.data.domain.Pageable;

import com.flab.mealmate.domain.meetup.entity.ProgressStatus;

import lombok.Getter;

@Getter
public class MeetupSearchCriteria {

	private final Pageable pageable;
	private final String keyword;
	private final Long cursorId;
	private final ProgressStatus progressStatus = SCHEDULED;

	public MeetupSearchCriteria(Pageable pageable, String keyword, Long cursorId) {
		this.pageable = pageable;
		this.keyword = keyword;
		this.cursorId = cursorId;
	}

	public int getPageSize() {
		return this.pageable.getPageSize();
	}
}
