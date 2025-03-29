package com.flab.mealmate.domain.meetup.entity;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import com.flab.mealmate.domain.meetup.policy.MeetupTimePolicy;
import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;

class MeetupScheduleTest {

	private final MeetupTimePolicy policy = new MeetupTimePolicy();

	@Test
	void 정상적으로_밋업일정이_생성된다() {
		LocalDateTime start = LocalDateTime.of(2026,1,1,12,0);
		LocalDateTime reference = start.minusHours(policy.minHoursBeforeStart());

		var schedule = MeetupSchedule.create(start, reference, policy);

		assertNotNull(schedule);
		assertEquals(start, schedule.getStartDateTime());
	}

	@Test
	void 시작시간이_정책보다_이전이면_에러() {
		LocalDateTime start = LocalDateTime.of(2026,1,1,12,0);
		LocalDateTime reference = start.minusHours(policy.minHoursBeforeStart()).plusMinutes(10);

		assertThrows(CustomIllegalArgumentException.class, () -> {
			MeetupSchedule.create(start, reference, policy);
		});
	}

	@Test
	void 모집마감시간은_정책기준_계산된다() {
		LocalDateTime start = LocalDateTime.of(2026,1,1,12,0);
		LocalDateTime reference = start.minusHours(policy.minHoursBeforeStart());
		LocalDateTime deadline = start.minusHours(policy.minHoursBeforeRecruitmentDeadline());

		var schedule = MeetupSchedule.create(start, reference, policy);

		assertNotNull(schedule);
		assertEquals(start, schedule.getStartDateTime());

		assertEquals(deadline, schedule.getRecruitmentDeadlineDateTime()
		);

	}

}