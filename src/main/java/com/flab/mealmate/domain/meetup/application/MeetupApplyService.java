package com.flab.mealmate.domain.meetup.application;

import com.flab.mealmate.domain.meetup.dto.MeetupApplyRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupApplyResponse;

public interface MeetupApplyService {
	MeetupApplyResponse apply(Long meetupId, MeetupApplyRequest request, Long userId);
}
