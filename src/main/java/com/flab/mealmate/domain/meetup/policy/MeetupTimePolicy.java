package com.flab.mealmate.domain.meetup.policy;

import org.springframework.stereotype.Component;

@Component
public class MeetupTimePolicy {
	public static final int MIN_HOURS_START = 2;
	public static final int MIN_HOURS_RECRUITMENT_DEADLINE = 1;

	public int minHoursBeforeStart() {
		return MIN_HOURS_START;
	}

	public int minHoursBeforeRecruitmentDeadline() {
		return MIN_HOURS_RECRUITMENT_DEADLINE;
	}

}
