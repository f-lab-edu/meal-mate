package com.flab.mealmate.global.error.exception;

import java.text.MessageFormat;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String[] stringArgList;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getValue());
		this.errorCode = errorCode;
		this.stringArgList = new String[] {};
	}

	public BusinessException(ErrorCode errorCode, String[] stringArgList) {
		super(getMessages(errorCode.getValue(), stringArgList));
		this.stringArgList = stringArgList;
		this.errorCode = errorCode;
	}

	private static String getMessages(String message, String[] stringArgList) {
		if (stringArgList != null && stringArgList.length > 0) {
			return MessageFormat.format(message, (Object[]) stringArgList);
		}
		return message;
	}
	public ErrorCode getErrorCode() {
		return this.errorCode;
	}
}
