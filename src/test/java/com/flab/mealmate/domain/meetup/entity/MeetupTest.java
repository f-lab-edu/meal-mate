package com.flab.mealmate.domain.meetup.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.flab.mealmate.domain.model.User;
import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.ErrorCode;

class MeetupTest {

	private static final String TITLE = "신촌에서 같이 밥먹을 사람";
	private static final String CONTENT = "샤브샤브 먹고싶어요.";
	private static final Long HOST_ID = 1L;
	private static final Long ANOTHER_USER_ID = 2L;
	private static final User HOST = new User(HOST_ID, "host");

	private MeetupSchedule dummySchedule() {
		return new MeetupSchedule();
	}

	private Meetup createValidMeetup(int min, int max) {
		var meetup = Meetup.create(TITLE, CONTENT, dummySchedule(), ParticipationType.AUTO, min, max);
		setCreatedBy(meetup, HOST);
		setCreatedBy(meetup.getParticipants().get(0), HOST);
		return meetup;
	}

	private void setCreatedBy(Object entity, User user) {
		ReflectionTestUtils.setField(entity, "createdBy", user);
	}

	@Test
	void createsMeetupSuccessfully() {
		var meetup = createValidMeetup(3, 5);

		assertNotNull(meetup);
		assertEquals(RecruitmentStatus.OPEN, meetup.getRecruitmentStatus());
		assertEquals(ProgressStatus.SCHEDULED, meetup.getProgressStatus());

		assertEquals(1, meetup.getParticipants().size());
		assertEquals(ParticipationStatus.APPROVED, meetup.getParticipants().get(0).getParticipationStatus());
	}

	@Test
	void throwsExceptionWhenMinParticipantsExceedsMax() {
		var expected = ErrorCode.ERR_MEETUP_002;

		BusinessException e = assertThrows(BusinessException.class, () -> {
			createValidMeetup(5, 3); // min > max
		});
		assertEquals(expected, e.getErrorCode());
	}

	@Test
	void addsAutoApprovedParticipantSuccessfullyWhenNoDuplicate() {
		Meetup meetup = createValidMeetup(3,5);

		// 첫번째 신청
		meetup.addAutoApprovedParticipant(ANOTHER_USER_ID);
		User another = new User(ANOTHER_USER_ID, "another");
		setCreatedBy(meetup.getParticipants().get(1), another);

		//두번째 신청
		assertThatThrownBy(() -> meetup.addAutoApprovedParticipant(ANOTHER_USER_ID))
			.isInstanceOf(BusinessException.class)
			.hasMessage(ErrorCode.ERR_MEETUP_PARTICIPANT_001.getValue());

	}

	@Test
	void throwsExceptionWhenAutoParticipantIsDuplicated() {
		Meetup meetup = createValidMeetup(3,5);

		assertThatThrownBy(() -> meetup.addAutoApprovedParticipant(HOST_ID))
			.isInstanceOf(BusinessException.class)
			.hasMessage(ErrorCode.ERR_MEETUP_PARTICIPANT_001.getValue());
	}

	@Test
	void addsPendingParticipantSuccessfullyWhenNoDuplicate() {
		Meetup meetup = createValidMeetup(3,5);

		meetup.addPendingParticipant("참여하고 싶어요!", ANOTHER_USER_ID);

		assertThat(meetup.getParticipants()).hasSize(2);
		assertThat(meetup.getParticipants().get(1).getParticipationStatus()).isEqualTo(ParticipationStatus.PENDING);
	}

	@Test
	void throwsExceptionWhenPendingParticipantIsDuplicated() {
		var meetup = createValidMeetup(3, 5);

		meetup.addPendingParticipant("한 번 신청함", ANOTHER_USER_ID);
		User another = new User(ANOTHER_USER_ID, "another");
		setCreatedBy(meetup.getParticipants().get(1), another);

		assertThatThrownBy(() -> meetup.addPendingParticipant("또 신청함", ANOTHER_USER_ID))
			.isInstanceOf(BusinessException.class)
			.hasMessage(ErrorCode.ERR_MEETUP_PARTICIPANT_001.getValue());
	}
}
