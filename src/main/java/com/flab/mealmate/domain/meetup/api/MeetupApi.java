package com.flab.mealmate.domain.meetup.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.mealmate.domain.meetup.application.MeetupCreateService;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupCreateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetups")
public class MeetupApi {

	private final MeetupCreateService meetupCreateService;

	@PostMapping
	public MeetupCreateResponse create(@RequestBody @Validated MeetupCreateRequest request) {
		return meetupCreateService.create(request);
	}

}
