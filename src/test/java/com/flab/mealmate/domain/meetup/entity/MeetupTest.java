package com.flab.mealmate.domain.meetup.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.ErrorCode;

class MeetupTest {

	@Test
	void 정상적으로_밋업이_생성된다() {
		var expectedRecruitmentStatus = RecruitmentStatus.OPEN;
		var expectedProgressStatus = ProgressStatus.SCHEDULED;

		var schedule = new MeetupSchedule();
		var meetup = new Meetup("신촌에서 같이 밥먹을 사람",  "샤브샤브 먹고싶어요.", schedule, ParticipationType.AUTO, 3,5);

		assertNotNull(meetup);
		assertEquals(expectedRecruitmentStatus, meetup.getRecruitmentStatus());
		assertEquals(expectedProgressStatus, meetup.getProgressStatus());
	}

	@Test
	void 최소_인원_수가_최대_인원_수보다_클_때_에러() {
		var schedule = new MeetupSchedule();
		var expected = ErrorCode.ERR_MEETUP_002;
		BusinessException e = assertThrows(BusinessException.class, () -> {
			new Meetup("신촌에서 같이 밥먹을 사람", "샤브샤브 먹고싶어요.", schedule, ParticipationType.AUTO, 5, 3);
		});

		assertEquals(expected, e.getErrorCode());
	}

}