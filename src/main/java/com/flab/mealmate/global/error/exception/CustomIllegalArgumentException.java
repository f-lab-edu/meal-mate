package com.flab.mealmate.global.error.exception;

public class CustomIllegalArgumentException extends IllegalArgumentException {
	
  private final ErrorCode errorCode;
	private final String[] stringArgList;

	public CustomIllegalArgumentException(ErrorCode errorCode) {
		super(errorCode.getValue());
		this.errorCode = errorCode;
		this.stringArgList = new String[] {};
	}

	public CustomIllegalArgumentException(ErrorCode errorCode, String[] stringArgList) {
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
