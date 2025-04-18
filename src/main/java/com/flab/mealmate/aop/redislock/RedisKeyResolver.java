package com.flab.mealmate.aop.redislock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

/**
 * Redis 키 생성 시 사용되는 도메인-식별자 조합 유틸리티
 * [domain]-[id]:[subdomain]-[subid]... 형태의 key 조합만 생성
 * 예: user-123:application-456
 */
public class RedisKeyResolver {

	private RedisKeyResolver() {
	}

	private static final String PREFIX_SEPARATOR = ":";
	private static final String DOMAIN_SEPARATOR = "-";
	private static final String SPEL_PREFIX = "#";

	/**
	 * 도메인-식별자 조합으로 구성된 Redis 키를 반환합니다.
	 *
	 * @param paramNames     메서드 파라미터 이름 배열
	 * @param args           파라미터 값 배열
	 * @param keyExpressions SpEL 표현식 배열 (예: "#userId", "#applicationId")
	 * @return user-123:application-456 와 같은 Redis 키 문자열 (prefix 제외)
	 */
	public static String buildKey(String[] paramNames, Object[] args, String... keyExpressions) {
		List<String> parts = new ArrayList<>();

		for (String expression : keyExpressions) {
			Object value = evaluateExpression(paramNames, args, expression);
			String domain = extractParamName(expression);
			parts.add(domain + DOMAIN_SEPARATOR + value);
		}

		return String.join(PREFIX_SEPARATOR, parts);
	}

	/**
	 * SpEL 표현식을 평가하여 값 반환
	 */
	private static Object evaluateExpression(String[] parameterNames, Object[] args, String key) {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();

		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i], args[i]);
		}

		Object result = parser.parseExpression(key).getValue(context, Object.class);

		if (result == null) {
			if (!isVariableDeclared(parameterNames, key)) {
				throw new CustomIllegalArgumentException(
					ErrorCode.ERR_REDIS_KEY_RESOLVE,
					new String[]{key}
				);
			}
			throw new CustomIllegalArgumentException(
				ErrorCode.ERR_REDIS_KEY_NULL,
				new String[]{key}
			);
		}

		return result;
	}

	/**
	 * 파라미터 이름 배열에 해당 변수가 존재하는지 확인합니다.
	 * 변수 선언 여부는 parameterNames 기준으로 확인합니다.
	 */
	private static boolean isVariableDeclared(String[] parameterNames, String expression) {
		String variableName = extractParamName(expression);
		for (String param : parameterNames) {
			if (param.equals(variableName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * SpEL 변수에서 '#' 기호를 제거해 도메인 이름을 추출합니다.
	 * 예: "#userId" → "userId"
	 */
	private static String extractParamName(String expression) {
		return expression.replace(SPEL_PREFIX, "");
	}
}
