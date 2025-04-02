package com.flab.mealmate.domain.meetup.entity;

public enum ParticipationStatus {

	PENDING("대기 중"),
	APPROVED("승인"),
	REJECTED("거절")
	;

	private String description;

	ParticipationStatus(String description) {
		this.description = description;
	}

	private String description() {
		return  this.description;
	}
}
