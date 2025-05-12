package com.flab.mealmate.infra.lock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

class LockExecutionContextTest {

	private final LockExecutor lockExecutor = mock(LockExecutor.class);

	@Test
	void throwExceptionWhenBuildKeyIsNotCalled() {
		// given
		LockExecutionContext context = LockExecutionContext.with(lockExecutor);

		assertThatThrownBy(() -> context.executeWithTask(() -> {
			// key가 없기 때문에 실행되지 않음
		}))
			.isInstanceOf(CustomIllegalArgumentException.class)
			.hasMessageContaining(ErrorCode.ERR_LOCK_KEY_003.name());
	}

	@Test
	void executeTaskSuccessfully() {
		LockExecutionContext context = LockExecutionContext.with(lockExecutor)
			.buildKey(new String[]{"meetup"}, new Object[]{1L});

		when(lockExecutor.execute(anyString(), anyInt(), anyInt(), any(TimeUnit.class), any(Supplier.class)))
			.thenReturn(true);

		boolean result = context.executeWithTask(() -> {});

		assertThat(result).isTrue();
		verify(lockExecutor, times(1)).execute(anyString(), anyInt(), anyInt(), any(), any());
	}

}