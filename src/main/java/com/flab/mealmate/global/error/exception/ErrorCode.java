package com.flab.mealmate.global.error.exception;

public enum ErrorCode {
	ERR_INVALID_INPUT_VALUE("ERR_INVALID_INPUT_VALUE"),
	ERR_METHOD_NOT_ALLOWED("ERR_METHOD_NOT_ALLOWED"),
	ERR_BUSINESS("ERR_BUSINESS"),
	ERR_FEIGN_CLIENT("ERR_FEIGN_CLIENT"),
	ERR_INVALID_TYPE_VALUE("ERR_INVALID_TYPE_VALUE"),
	ERR_DB("ERR_DB"),
	ERR_ENTITY_NOT_FOUND("ERR_ENTITY_NOT_FOUND"),
	ERR_INTERNAL_SERVER_ERROR("ERR_INTERNAL_SERVER_ERROR"),

	// Redis
	ERR_REDIS_KEY_RESOLVE("ERR_REDIS_KEY_RESOLVE"),
	ERR_REDIS_KEY_NULL("ERR_REDIS_KEY_NULL"),

	// 모임
	ERR_MEETUP_001("ERR_MEETUP_001"),
	ERR_MEETUP_002("ERR_MEETUP_002"),
	;


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
