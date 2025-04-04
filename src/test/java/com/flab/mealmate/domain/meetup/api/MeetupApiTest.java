package com.flab.mealmate.domain.meetup.api;

import static com.flab.mealmate.global.ApiDocumentation.field;
import static com.flab.mealmate.global.ApiDocumentation.parameter;
import static com.flab.mealmate.global.util.JsonUtils.objectMapper;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.epages.restdocs.apispec.SimpleType;
import com.flab.mealmate.domain.meetup.application.MeetupCreateService;
import com.flab.mealmate.domain.meetup.application.MeetupSearchService;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateResponse;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResponse;
import com.flab.mealmate.domain.meetup.dto.MeetupSummary;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.domain.meetup.entity.RecruitmentStatus;
import com.flab.mealmate.global.ApiDocumentation;


@WithMockUser
@AutoConfigureRestDocs
@WebMvcTest(MeetupApi.class)
class MeetupApiTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MeetupCreateService meetupCreateService;

	@MockitoBean
	private MeetupSearchService meetupSearchService;

	private final String TAG = "meetup";

	@Test
	@DisplayName("모임 생성 API")
	void createMeetup() throws Exception {
		var request = new MeetupCreateRequest(
			"신촌에서 같이 밥먹을 사람",
			"샤브샤브 먹고싶어요.",
			ParticipationType.AUTO,
			LocalDateTime.of(2026, 1, 1, 12, 0, 0, 0),
			3,
			5
		);

		var response = new MeetupCreateResponse("1");

		// Mocking the service call
		given(meetupCreateService.create(any(MeetupCreateRequest.class))).willReturn(response);

		// Perform the API request
		ResultActions actions = mockMvc.perform(post("/meetups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf().asHeader()))
			.andExpect(status().isOk());

		actions.andDo(ApiDocumentation.builder()
				.tag(TAG)
				.description("모임 생성 API")
				.requestFields(
					field("title", STRING, "모임 제목"),
					field("content", STRING, "모임 내용"),
					field("participationType", STRING, "참여 유형" ),
					field("startDateTime", STRING, "시작 시간"),
					field("minParticipants", NUMBER, "최소 인원"),
					field("maxParticipants", NUMBER, "최대 인원")
				)
				.responseFields(
					field("id", STRING, "생성된 모임 ID")
				)
				.build())
			.andDo(print());
	}

	@Test
	@DisplayName("모임 검색 API")
	void search() throws Exception {
		var meetupSummary = new MeetupSummary(1L, "신촌에서 밥먹어요", "샤브샤브 각", LocalDateTime.of(2026, 1, 1, 12, 0, 0),
			ParticipationType.AUTO, RecruitmentStatus.OPEN, 3L);
		var response = new MeetupSearchResponse(1L, "1", List.of(meetupSummary));

		given(meetupSearchService.search(any(MeetupSearchRequest.class)))
			.willReturn(response);

		ResultActions actions = mockMvc.perform(get("/meetups")
				.param("keyword", "신촌")
				.param("size", "10")
				.param("cursorId", "100")
				.with(csrf().asHeader())
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk());

		actions.andDo(ApiDocumentation.builder()
				.tag(TAG)
				.description("모임 검색 API")
				.requestParameters(
					parameter("keyword", SimpleType.STRING, "검색 키워드").optional(),
					parameter("size", SimpleType.NUMBER, "페이지 크기 (최대 100)"),
					parameter("cursorId", SimpleType.NUMBER, "커서 ID (마지막 항목 기준)").optional()
				)
				.responseFields(
					field("totalCount", NUMBER, "전체 모임 개수"),
					field("cursorId", STRING, "마지막 항목의 ID"),
					field("meetups[].id", STRING, "모임 ID"),
					field("meetups[].title", STRING, "모임 제목"),
					field("meetups[].description", STRING, "모임 설명"),
					field("meetups[].startDateTime", STRING, "모임 시작 시간"),
					field("meetups[].participationType", STRING, "참여 방식"),
					field("meetups[].recruitmentStatus", STRING, "모집 상태"),
					field("meetups[].participants", NUMBER, "참여 인원 수")
				)
				.build()
			)
			.andDo(print());
	}
}