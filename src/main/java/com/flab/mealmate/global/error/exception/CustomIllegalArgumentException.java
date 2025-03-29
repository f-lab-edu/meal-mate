package com.flab.mealmate.global.error.exception;

public class CustomIllegalArgumentException extends IllegalArgumentException{

	private final ErrorCode errorCode;

	public CustomIllegalArgumentException(ErrorCode errorCode, Object[] args) {
		super(String.format(errorCode.getValue(), (Object[]) args));
		this.errorCode = errorCode;
	}

}
