package com.flab.mealmate.domain.meetup.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.flab.mealmate.domain.meetup.dto.MeetupCreateRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateResponse;
import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.MeetupSchedule;
import com.flab.mealmate.domain.meetup.policy.MeetupTimePolicy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetupCreateMapper {

	public Meetup toEntity(MeetupCreateRequest request, LocalDateTime now, MeetupTimePolicy policy) {
		var schedule = MeetupSchedule.create(request.getStartDateTime(), now, policy);
		return new Meetup(
			request.getTitle(),
			request.getContent(),
			schedule,
			request.getParticipationType(),
			request.getMinParticipants(),
			request.getMaxParticipants()
		);
	}

	public MeetupCreateResponse toResponse(Meetup meetup) {
		return new MeetupCreateResponse(String.valueOf(meetup.getId()));
	}
}
