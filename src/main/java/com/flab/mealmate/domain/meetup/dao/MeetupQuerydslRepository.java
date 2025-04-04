package com.flab.mealmate.domain.meetup.dao;

import static com.flab.mealmate.domain.meetup.entity.QMeetup.*;
import static com.flab.mealmate.domain.meetup.entity.QMeetupParticipant.*;
import static com.flab.mealmate.global.util.querydsl.BooleanExpressionUtils.*;

import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.flab.mealmate.domain.meetup.dto.MeetupSearchCriteria;
import com.flab.mealmate.domain.meetup.dto.MeetupSearchResult;
import com.flab.mealmate.domain.meetup.dto.QMeetupSearchResult;
import com.flab.mealmate.domain.meetup.entity.ProgressStatus;
import com.flab.mealmate.global.util.querydsl.FullTextSearchUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MeetupQuerydslRepository implements CustomMeetupRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<MeetupSearchResult> page(MeetupSearchCriteria criteria) {
		var content = queryFactory
			.select(new QMeetupSearchResult(
				meetup.id,
				meetup.title,
				meetup.content,
				meetup.schedule.startDatetime,
				meetup.participationType,
				meetup.recruitmentStatus,
				meetupParticipant.id.count()
			))
			.from(meetup)
			.leftJoin(meetupParticipant)
			.on(meetupParticipant.meetup.id.eq(meetup.id))
			.where(
				eqProgressStatus(criteria.getProgressStatus()),
				ltId(criteria.getCursorId()),
				searchFullText(criteria.getKeyword())
			)
			.orderBy(meetup.id.desc())
			.limit(criteria.getPageSize())
			.fetch();

		var countQuery = queryFactory
			.select(meetup.id.count())
			.from(meetup)
			.where(
				eqProgressStatus(criteria.getProgressStatus()),
				ltId(criteria.getCursorId()),
				searchFullText(criteria.getKeyword())
			);

		return PageableExecutionUtils.getPage(content, criteria.getPageable(), countQuery::fetchOne);
	}

	private Predicate ltId(Long cursorId) {
		return nullSafeBuilder(() -> meetup.id.lt(cursorId));
	}

	private Predicate searchFullText(String keyword) {
		return FullTextSearchUtils.fullTextSearchBuilder(meetup.searchText, keyword);
	}

	private Predicate eqProgressStatus(ProgressStatus progressStatus) {
		return nullSafeBuilder(() -> meetup.progressStatus.eq(progressStatus));
	}

}
