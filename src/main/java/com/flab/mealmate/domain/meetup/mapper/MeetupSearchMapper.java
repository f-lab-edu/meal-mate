package com.flab.mealmate.domain.meetup.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.flab.mealmate.domain.meetup.dto.MeetupSearchCriteria;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResponse;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResult;
import com.flab.mealmate.domain.meetup.dto.MeetupSummary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetupSearchMapper {

	public MeetupSearchCriteria toCriteria(MeetupSearchRequest request) {
		return new MeetupSearchCriteria(PageRequest.ofSize(request.getSize()), request.getKeyword(), request.getCursorId());
	}

	public MeetupSearchResponse toResponse(Page<MeetupSearchResult> page) {
		var meetups = page.getContent().stream()
			.map(this::toSummary)
			.toList();

		var cursorId =  meetups.isEmpty() ? null : meetups.getLast().getId();

		return new MeetupSearchResponse(page.getTotalElements(), cursorId, meetups);
	}

	private MeetupSummary toSummary(MeetupSearchResult row) {
		return new MeetupSummary(
			row.getId(),
			row.getTitle(),
			row.getDescription(),
			row.getStartDateTime(),
			row.getParticipationType(),
			row.getRecruitmentStatus(),
			row.getParticipants()
		);
	}
}
