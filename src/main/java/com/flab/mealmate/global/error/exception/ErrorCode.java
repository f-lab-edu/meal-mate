package com.flab.mealmate.global.error.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	ERR_INVALID_INPUT_VALUE("ERR_INVALID_INPUT_VALUE"),
	ERR_METHOD_NOT_ALLOWED("ERR_METHOD_NOT_ALLOWED"),
	ERR_BUSINESS("ERR_BUSINESS"),
	ERR_FEIGN_CLIENT("ERR_FEIGN_CLIENT"),
	ERR_INVALID_TYPE_VALUE("ERR_INVALID_TYPE_VALUE"),
	ERR_DB("ERR_DB"),
	ERR_ENTITY_NOT_FOUND("ERR_ENTITY_NOT_FOUND"),
	ERR_INTERNAL_SERVER_ERROR("ERR_INTERNAL_SERVER_ERROR"),

	// 모임
	ERR_MEETUP_001("시작 시간은 현재 시각 기준 최소 %d시간 이후여야 합니다.");


	private final String value;
	private String text;

	ErrorCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	ErrorCode(String value, String text) {
		this.value = value;
		this.text = text;
	}
}
