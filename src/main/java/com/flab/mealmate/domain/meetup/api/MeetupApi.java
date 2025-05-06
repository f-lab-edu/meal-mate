package com.flab.mealmate.domain.meetup.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.mealmate.domain.meetup.application.MeetupApplyService;
import com.flab.mealmate.domain.meetup.application.MeetupCreateService;
import com.flab.mealmate.domain.meetup.application.MeetupSearchService;
import com.flab.mealmate.domain.meetup.dto.MeetupApplyRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateResponse;
import com.flab.mealmate.domain.meetup.dto.MeetupApplyResponse;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResponse;
import com.flab.mealmate.global.config.security.AuthenticationUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetups")
public class MeetupApi {

	private final MeetupCreateService meetupCreateService;
	private final MeetupSearchService meetupSearchService;
	private final MeetupApplyService meetupApplyService;

	@PostMapping
	public MeetupCreateResponse create(@RequestBody @Validated MeetupCreateRequest request) {
		return meetupCreateService.create(request);
	}

	@GetMapping
	public MeetupSearchResponse search(@ModelAttribute @Validated MeetupSearchRequest request) {
		return meetupSearchService.search(request);
	}

	@PostMapping("/{meetupId}/applications")
	public MeetupApplyResponse apply(@PathVariable Long meetupId, @RequestBody MeetupApplyRequest request) {
		return meetupApplyService.apply(meetupId, request, AuthenticationUtils.getUserId());
	}

}
