package com.flab.mealmate.domain.meetup.entity;

import lombok.Getter;

@Getter
public enum ParticipationStatus {

	PENDING("대기 중", true),
	APPROVED("승인", true),
	REJECTED("거절", false)
	;

	private final String description;
	private final boolean active;

	ParticipationStatus(String description, boolean active) {
		this.description = description;
		this.active = active;
	}

	public boolean isApproved() {
		return this == ParticipationStatus.APPROVED;
	}

}