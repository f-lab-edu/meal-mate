package com.flab.mealmate.domain.meetup.entity;


public enum ParticipationType {
	AUTO("누구나 참여 가능"),
	APPROVAL("승인을 받아야 참여 가능");

	private String description;

	ParticipationType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

}
