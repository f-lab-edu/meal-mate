package com.flab.mealmate.domain.meetup.entity;


public enum ProgressStatus {
	SCHEDULED ("예정"),
	COMPLETED ("종료"),
	CANCELED ("취소됨");

	private String description;

	ProgressStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
