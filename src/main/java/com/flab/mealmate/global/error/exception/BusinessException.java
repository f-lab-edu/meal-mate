package com.flab.mealmate.global.error.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.flab.mealmate.global.error.exception.ErrorCode;

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
		super(getMessages(stringArgList));
		this.stringArgList = stringArgList;
		this.errorCode = errorCode;
	}



	private static String getMessages(String[] stringArgList) {
		if (stringArgList != null && stringArgList.length > 0) {
			return Arrays.stream(stringArgList)
				.collect(Collectors.joining(", "));
		}
		return "";
	}
}
