package com.flab.mealmate.aop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

class RedisKeyResolverTest {
	@Test
	void shouldBuildKeyWithSingleParameter() {
		// given
		String[] parameterNames = {"meetupId", "userId"};
		Object[] args = {123L, 456L};
		String spelKey = "#meetupId";

		// when
		Object result = RedisKeyResolver.buildKey(parameterNames, args, spelKey);

		// then
		assertThat(result).isEqualTo("meetupId-123");
	}

	@Test
	void shouldBuildKeyWithMultipleParameters() {
		// given
		String[] parameterNames = {"userId", "applicationId"};
		Object[] args = {456L, 789L};
		String[] spelKey = {"#userId", "#applicationId"};

		// when
		String result = RedisKeyResolver.buildKey(parameterNames, args, spelKey);

		// then
		assertThat(result).isEqualTo("userId-456:applicationId-789");
	}

	@Test
	void throwsExceptionWhenSpelKeyDoesNotExist() {
		// given
		String[] parameterNames = {"userId"};
		Object[] args = {123L};

		// when & then
		assertThatThrownBy(() ->
			RedisKeyResolver.buildKey(parameterNames, args, "#unknownKey")
		)
			.isInstanceOf(CustomIllegalArgumentException.class)
			.hasMessage(ErrorCode.ERR_REDIS_KEY_RESOLVE.getValue());
	}

	@Test
	void throwsExceptionWhenKeyValueIsNull() {
		// given
		String[] parameterNames = {"userId"};
		Object[] args = {null};

		// when & then
		assertThatThrownBy(() ->
			RedisKeyResolver.buildKey(parameterNames, args, "#userId")
		)
			.isInstanceOf(CustomIllegalArgumentException.class)
			.hasMessage(ErrorCode.ERR_REDIS_KEY_NULL.getValue());
	}


}