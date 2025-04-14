package com.flab.mealmate.domain.meetup.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.domain.meetup.entity.RecruitmentStatus;

import lombok.Getter;

@Getter
public class MeetupSummary {

	private final String id;
	private final String title;
	private final String description;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime startDateTime;
	private final ParticipationType participationType;
	private final RecruitmentStatus recruitmentStatus;
	private final long participants;

	public MeetupSummary(Long id, String title, String description, LocalDateTime startDateTime,
		ParticipationType participationType, RecruitmentStatus recruitmentStatus, long participants) {
		this.id = String.valueOf(id);
		this.title = title;
		this.description = description;
		this.startDateTime = startDateTime;
		this.participationType = participationType;
		this.recruitmentStatus = recruitmentStatus;
		this.participants = participants;
	}

}
