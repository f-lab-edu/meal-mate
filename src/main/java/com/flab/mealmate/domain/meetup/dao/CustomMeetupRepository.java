package com.flab.mealmate.domain.meetup.dao;

import org.springframework.data.domain.Page;

import com.flab.mealmate.domain.meetup.dto.MeetupSearchCriteria;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResult;

public interface CustomMeetupRepository {

	Page<MeetupSearchResult> page(MeetupSearchCriteria criteria);

}
