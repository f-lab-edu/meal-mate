package com.flab.mealmate.domain.meetup.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.flab.mealmate.domain.model.User;

class MeetupParticipantTest {

	private static final Long USER_ID = 1L;
	private static final Long OTHER_USER_ID = 999L;
	private static final User CREATOR = new User(USER_ID, "tester");

	private Meetup dummyMeetup() {
		var schedule = new MeetupSchedule();
		return Meetup.create("신촌에서 같이 밥먹을 사람", "샤브샤브 먹고싶어요.", schedule, ParticipationType.AUTO, 3, 5);
	}

	private MeetupParticipant createParticipant(ParticipationStatus status) {
		var participant = MeetupParticipant.createParticipant(dummyMeetup(), status);
		ReflectionTestUtils.setField(participant, "createdBy", CREATOR);
		return participant;
	}

	@Test
	void recognizesApprovedParticipantAsActive() {
		var participant = createParticipant(ParticipationStatus.APPROVED);
		boolean result = participant.isActiveParticipant(USER_ID);

		assertThat(result).isTrue();
	}

	@Test
	void recognizesPendingParticipantAsActive() {
		var participant = createParticipant(ParticipationStatus.PENDING);
		boolean result = participant.isActiveParticipant(USER_ID);

		assertThat(result).isTrue();
	}

	@Test
	void doesNotRecognizeRejectedAsActive() {
		var participant = createParticipant(ParticipationStatus.REJECTED);
		boolean result = participant.isActiveParticipant(USER_ID);

		assertThat(result).isFalse();
	}

	@Test
	void doesNotRecognizeDifferentUserAsActive() {
		var participant = createParticipant(ParticipationStatus.APPROVED);
		boolean result = participant.isActiveParticipant(OTHER_USER_ID);

		assertThat(result).isFalse();
	}
}
