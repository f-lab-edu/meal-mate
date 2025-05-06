package com.flab.mealmate.domain.meetup.application;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.mealmate.domain.meetup.application.participation.ParticipationStrategy;
import com.flab.mealmate.domain.meetup.dao.MeetupRepository;
import com.flab.mealmate.domain.meetup.dto.MeetupApplyRequest;
import com.flab.mealmate.domain.meetup.dto.MeetupApplyResponse;
import com.flab.mealmate.domain.meetup.entity.Meetup;
import com.flab.mealmate.domain.meetup.entity.ParticipationType;
import com.flab.mealmate.domain.meetup.mapper.MeetupApplyMapper;
import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

@Service
@Transactional
public class MeetupApplyServiceV1 implements MeetupApplyService {

	private final Map<ParticipationType, ParticipationStrategy> strategyMap;
	private final MeetupRepository meetupRepository;

	public MeetupApplyServiceV1(List<ParticipationStrategy> strategies, MeetupRepository meetupRepository) {
		this.meetupRepository = meetupRepository;
		this.strategyMap = strategies.stream()
			.collect(Collectors.toMap(ParticipationStrategy::getType, Function.identity()));
	}

	@Override
	public MeetupApplyResponse apply(Long meetupId, MeetupApplyRequest request, Long userId) {
		Meetup meetup = meetupRepository.findById(meetupId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ERR_ENTITY_NOT_FOUND));

		ParticipationStrategy strategy = ofNullable(strategyMap.get(meetup.getParticipationType()))
			.orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.ERR_MEETUP_PARTICIPANT_003));

		strategy.participate(meetup, request.getMessage(), userId);

		return MeetupApplyMapper.toResponse(meetup);
	}
}
