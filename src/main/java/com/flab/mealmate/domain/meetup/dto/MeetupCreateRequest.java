package com.flab.mealmate.domain.meetup.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MeetupCreateRequest {

	@NotEmpty
	private final String title;

	@NotEmpty
	private final String content;

	@NotNull
	private final ParticipationType participationType;

	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime startDateTime;

	@NotNull
	@Min(value = 2) @Max(value = 200)
	private final int minParticipants;

	@NotNull
	@Min(value = 2)  @Max(value = 200)
	private final int maxParticipants;

	public MeetupCreateRequest(String title, String content, ParticipationType participationType,
		LocalDateTime startDateTime, int minParticipants, int maxParticipants) {
		this.title = title;
		this.content = content;
		this.participationType = participationType;
		this.startDateTime = startDateTime;
		this.minParticipants = minParticipants;
		this.maxParticipants = maxParticipants;
		validateParticipants();
	}

	private void validateParticipants() {
		if (this.maxParticipants < this.minParticipants) {
			throw new CustomIllegalArgumentException(ErrorCode.ERR_MEETUP_002);
		}
	}
}
