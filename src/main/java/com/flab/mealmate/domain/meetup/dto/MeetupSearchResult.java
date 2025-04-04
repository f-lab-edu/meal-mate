package com.flab.mealmate.domain.meetup.dto;

import java.time.LocalDateTime;

import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.domain.meetup.entity.RecruitmentStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class MeetupSearchResult {

	private final Long id;
	private final String title;
	private final String description;
	private final LocalDateTime startDateTime;
	private final ParticipationType participationType;
	private final RecruitmentStatus recruitmentStatus;
	private final long participants;

	@QueryProjection
	public MeetupSearchResult(Long id, String title, String description, LocalDateTime startDateTime,
		ParticipationType participationType, RecruitmentStatus recruitmentStatus, long participants) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.startDateTime = startDateTime;
		this.participationType = participationType;
		this.recruitmentStatus = recruitmentStatus;
		this.participants = participants;
	}
}
