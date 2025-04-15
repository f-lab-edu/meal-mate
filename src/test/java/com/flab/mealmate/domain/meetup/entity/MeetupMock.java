package com.flab.mealmate.domain.meetup.entity;

import org.springframework.test.util.ReflectionTestUtils;

import com.flab.mealmate.domain.model.User;

public class MeetupMock {
	private static final String TITLE = "신촌에서 같이 밥먹을 사람";
	private static final String CONTENT = "샤브샤브 먹고싶어요.";
	private static final User HOST = new User(1L, "host");

	public static Meetup createAuto(int min, int max) {
		Meetup meetup = Meetup.create(TITLE, CONTENT, dummySchedule(), ParticipationType.AUTO, min, max);
		setCreatedBy(meetup, HOST);
		setCreatedBy(meetup.getParticipants().get(0), HOST); // 호스트 참가자
		return meetup;
	}

	public static Meetup createApproval(int min, int max) {
		Meetup meetup = Meetup.create(TITLE, CONTENT, dummySchedule(), ParticipationType.APPROVAL, min, max);
		setCreatedBy(meetup, HOST);
		setCreatedBy(meetup.getParticipants().get(0), HOST);
		return meetup;
	}

	private static MeetupSchedule dummySchedule() {
		return new MeetupSchedule(); // 혹은 필요 시 fixed datetime
	}

	private static void setCreatedBy(Object entity, User user) {
		ReflectionTestUtils.setField(entity, "createdBy", user);
	}
}
