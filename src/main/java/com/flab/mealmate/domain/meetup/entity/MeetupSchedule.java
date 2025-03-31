package com.flab.mealmate.domain.meetup.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.flab.mealmate.domain.meetup.policy.MeetupTimePolicy;
import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.ErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetupSchedule {

	@Column(nullable = false)
	@Comment("시작 시간")
	private LocalDateTime startDateTime;

	@Column(nullable = false)
	@Comment("모집 마감 시간")
	private LocalDateTime recruitmentDeadlineDateTime;

	private MeetupSchedule(LocalDateTime startDateTime, LocalDateTime recruitmentDeadlineDateTime) {
		this.startDateTime = startDateTime;
		this.recruitmentDeadlineDateTime = recruitmentDeadlineDateTime;
	}

	public static MeetupSchedule create(LocalDateTime startDateTime, LocalDateTime now, MeetupTimePolicy policy) {
		MeetupSchedule schedule = new MeetupSchedule(
			startDateTime,
			startDateTime.minusHours(policy.minHoursBeforeRecruitmentDeadline())
		);

		schedule.validateDateTime(now, policy.minHoursBeforeStart());
		return schedule;
	}

	private void validateDateTime(LocalDateTime now, int minHoursBeforeStart) {
		if (this.startDateTime.isBefore(now.plusHours(minHoursBeforeStart))) {
			throw new BusinessException(ErrorCode.ERR_MEETUP_001, new String[]{String.valueOf(minHoursBeforeStart)});
		}
	}
}