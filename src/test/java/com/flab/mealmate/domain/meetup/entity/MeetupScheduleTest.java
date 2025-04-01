package com.flab.mealmate.domain.meetup.entity;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import com.flab.mealmate.domain.meetup.policy.MeetupTimePolicy;
import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.ErrorCode;

class MeetupScheduleTest {

	private final MeetupTimePolicy policy = new MeetupTimePolicy();

	@Test
	void creates_meetup_schedule_successfully() {
		LocalDateTime start = LocalDateTime.of(2026,1,1,12,0);
		LocalDateTime reference = start.minusHours(policy.minHoursBeforeStart());

		var schedule = MeetupSchedule.create(start, reference, policy);

		assertNotNull(schedule);
		assertEquals(start, schedule.getStartDatetime());
	}

	@Test
	void throws_exception_when_start_time_is_too_early() {
		LocalDateTime start = LocalDateTime.of(2026,1,1,12,0);
		LocalDateTime reference = start.minusHours(policy.minHoursBeforeStart()).plusMinutes(10);

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			MeetupSchedule.create(start, reference, policy);
		});

		assertEquals(ErrorCode.ERR_MEETUP_001, exception.getErrorCode());
	}

	@Test
	void calculates_recruitment_deadline_based_on_policy() {
		LocalDateTime start = LocalDateTime.of(2026,1,1,12,0);
		LocalDateTime reference = start.minusHours(policy.minHoursBeforeStart());
		LocalDateTime deadline = start.minusHours(policy.minHoursBeforeRecruitmentDeadline());

		var schedule = MeetupSchedule.create(start, reference, policy);

		assertNotNull(schedule);
		assertEquals(start, schedule.getStartDatetime());

		assertEquals(deadline, schedule.getRecruitmentDeadlineDatetime()
		);

	}

}