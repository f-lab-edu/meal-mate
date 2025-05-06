package com.flab.mealmate.infra.lock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

class CompositeKeyGeneratorTest {

	@Test
	void buildKeySuccessfully() {

		String[] keys = {"meetup"};
		Object[] values = {123};
		String actual = CompositeKeyGenerator.buildKey(keys, values);

		assertEquals("LOCK:meetup-123", actual);
	}

	@Test
	void buildMultiKeysSuccessfully() {
		String[] keys = {"stock", "item"};
		Object[] values = {123, 456};

		String result = CompositeKeyGenerator.buildKey(keys, values);

		assertEquals("LOCK:stock-123:item-456", result);
	}

	@Test
	void throwExceptionWhenValuesIsNull() {
		String[] keys = {"stock"};
		Object[] values = null;

		// when & then
		assertThatThrownBy(() -> CompositeKeyGenerator.buildKey(keys, values))
			.isInstanceOf(CustomIllegalArgumentException.class)
			.hasMessageContaining(ErrorCode.ERR_LOCK_KEY_001.getValue());
	}

	@Test
	void throwExceptionWhenKeysIsNull() {
		String[] keys = null;
		Object[] values = {123};

		// when & then
		assertThatThrownBy(() -> CompositeKeyGenerator.buildKey(keys, values))
			.isInstanceOf(CustomIllegalArgumentException.class)
			.hasMessageContaining(ErrorCode.ERR_LOCK_KEY_001.getValue());
	}

	@Test
	void throwExceptionWhenKeysAndValuesLengthMismatch() {
		String[] keys = {"stock", "item"};
		Object[] values = {123};

		assertThatThrownBy(() -> CompositeKeyGenerator.buildKey(keys, values))
			.isInstanceOf(CustomIllegalArgumentException.class)
			.hasMessageContaining(ErrorCode.ERR_LOCK_KEY_002.getValue());
	}

}