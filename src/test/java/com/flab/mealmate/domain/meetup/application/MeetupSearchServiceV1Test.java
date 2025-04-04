package com.flab.mealmate.domain.meetup.application;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import com.flab.mealmate.domain.meetup.dao.CustomMeetupRepository;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchCriteria;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResponse;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResult;
import com.flab.mealmate.domain.meetup.dto.MeetupSummary;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.domain.meetup.entity.RecruitmentStatus;
import com.flab.mealmate.domain.meetup.mapper.MeetupSearchMapper;

@ExtendWith(MockitoExtension.class)
class MeetupSearchServiceV1Test {

	@Mock
	private CustomMeetupRepository meetupRepository;

	@Mock
	private MeetupSearchMapper meetupSearchMapper;

	@InjectMocks
	private MeetupSearchServiceV1 meetupSearchService;

	@Test
	void search() {
		var request = new MeetupSearchRequest("신촌", 100, null);
		var criteria = new MeetupSearchCriteria(PageRequest.of(0, request.getSize()), request.getKeyword(), request.getCursorId());

		var result = new MeetupSearchResult(1L, "신촌에서 밥먹을사람", "파스타 먹고싶어요", LocalDateTime.of(2026,1,1,12,0),
			ParticipationType.APPROVAL, RecruitmentStatus.OPEN, 1);
		var page = PageableExecutionUtils.getPage(List.of(result), criteria.getPageable(), () -> 1L);

		var summary = new MeetupSummary(result.getId(), result.getTitle(), result.getDescription(), result.getStartDateTime(),
			result.getParticipationType(), result.getRecruitmentStatus(), result.getParticipants());
		var response = new MeetupSearchResponse(1L, "1", List.of(summary));

		given(meetupSearchMapper.toCriteria(request)).willReturn(criteria);
		given(meetupRepository.page(criteria)).willReturn(page);
		given(meetupSearchMapper.toResponse(page)).willReturn(response);

		meetupSearchService.search(request);

		verify(meetupSearchMapper).toCriteria(request);
		verify(meetupRepository).page(criteria);
		verify(meetupSearchMapper).toResponse(page);

	}

}