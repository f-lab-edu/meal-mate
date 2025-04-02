package com.flab.mealmate.domain.meetup.entity;

public enum RecruitmentStatus {
	OPEN("모집중"),
	CLOSED("모집 마감"),
	PAUSED("모집 일시 중단")
	;

	private String description;

	RecruitmentStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
