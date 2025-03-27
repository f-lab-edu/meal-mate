package com.flab.mealmate.global.error.exception;

public class CustomIllegalArgumentException extends IllegalArgumentException {

	private static final String DEFAULT_ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "올바르지 않은 입력값입니다.";

	public CustomIllegalArgumentException() {
		super(DEFAULT_ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
	}

	public CustomIllegalArgumentException(String message) {
		super(message);
	}

	public CustomIllegalArgumentException(String message, Throwable cause) {
		super(message, cause);
	}
}
