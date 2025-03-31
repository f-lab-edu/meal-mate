package com.flab.mealmate.domain.meetup.application;

import com.flab.mealmate.domain.meetup.dto.MeetupCreateRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateResponse;

public interface MeetupCreateService {

	MeetupCreateResponse create(MeetupCreateRequest request);

}
