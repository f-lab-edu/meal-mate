package com.flab.mealmate.global.util.querydsl;

import org.springframework.util.StringUtils;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringPath;

public class FullTextSearchUtils {

	private static final String MATCH_QUERY_TEMPLATE = "match({0}) against ({1} in boolean mode)";
	private static final double MINIMUM_MATCH_SCORE = 0.0;

	public static Predicate fullTextSearchBuilder(StringPath fullTextColumn, String keyword) {
		if (!StringUtils.hasText(keyword)) {
			return null; // 키워드가 없으면 null 반환
		}

		NumberTemplate<Double> score = Expressions.numberTemplate(
			Double.class,
			MATCH_QUERY_TEMPLATE,
			fullTextColumn,
			Expressions.constant(keyword)
		);

		return score.gt(MINIMUM_MATCH_SCORE);
	}
}
