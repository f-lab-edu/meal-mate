package com.flab.mealmate.domain.meetup.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import com.flab.mealmate.domain.meetup.dto.MeetupSearchRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResult;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.domain.meetup.entity.RecruitmentStatus;

class MeetupSearchMapperTest {

	private final MeetupSearchMapper mapper = new MeetupSearchMapper();

	@Test
	void toCriteriaMapsRequestToCriteriaSuccessfully() {
		var request = new MeetupSearchRequest("신촌", 10, 100L);

		var criteria = mapper.toCriteria(request);

		assertEquals(request.getKeyword(), criteria.getKeyword());
		assertEquals(request.getSize(), criteria.getPageable().getPageSize());
		assertEquals(request.getCursorId(), criteria.getCursorId());
	}

	@Test
	void toCriteriaMapsRequestWithNullCursorId() {
		var request = new MeetupSearchRequest("신촌", 10, null);

		var criteria = mapper.toCriteria(request);

		assertNull(criteria.getCursorId());
	}

	@Test
	void toResponseMapsEmptyPageWithNullCursorId() {
		var expectedTotalCount = 0L;
		var pageable = PageRequest.of(0, 10);

		Page<MeetupSearchResult> emptyPage = PageableExecutionUtils.getPage(List.of(), pageable, () -> 0L);

		var response = mapper.toResponse(emptyPage);

		assertNotNull(response);
		assertEquals(expectedTotalCount, response.getTotalCount());
		assertNull(response.getCursorId());
		assertTrue(response.getMeetups().isEmpty());
	}

	@Test
	void toResponseMapsPageWithLastCursorId() {
		var pageable = PageRequest.of(0, 10);
		var result = new MeetupSearchResult(
			123L,
			"신촌에서 밥먹어요",
			"샤브샤브 각",
			LocalDateTime.of(2026, 1, 1, 12, 0),
			ParticipationType.AUTO,
			RecruitmentStatus.OPEN,
			3
		);


		var page = PageableExecutionUtils.getPage(List.of(result), pageable, () -> 1L);

		var expectedCursorId = "123";
		var expectedTotalCount = 1L;

		var response = mapper.toResponse(page);

		assertNotNull(response);
		assertEquals(expectedTotalCount, response.getTotalCount());
		assertEquals(expectedCursorId, response.getCursorId());
		assertEquals(1, response.getMeetups().size());
		assertEquals(result.getTitle(), response.getMeetups().get(0).getTitle());
	}
}


