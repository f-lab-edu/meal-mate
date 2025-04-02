package com.flab.mealmate.domain.meetup.api;

import static com.flab.mealmate.global.ApiDocumentation.field;
import static com.flab.mealmate.global.util.JsonUtils.objectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

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

import com.flab.mealmate.domain.meetup.application.MeetupCreateService;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateResponse;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.global.ApiDocumentation;

@WithMockUser
@AutoConfigureRestDocs
@WebMvcTest(MeetupApi.class)
class MeetupApiTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MeetupCreateService meetupCreateService;

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

}