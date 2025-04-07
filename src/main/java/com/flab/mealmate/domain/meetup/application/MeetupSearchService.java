package com.flab.mealmate.domain.meetup.application;

import com.flab.mealmate.domain.meetup.dto.MeetupSearchRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResponse;

public interface MeetupSearchService {

	MeetupSearchResponse search(MeetupSearchRequest request);
}
