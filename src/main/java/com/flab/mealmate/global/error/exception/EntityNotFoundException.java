package com.flab.mealmate.global.error.exception;


public class EntityNotFoundException extends BusinessException {

	public EntityNotFoundException() {
		super(ErrorCode.ERR_ENTITY_NOT_FOUND);
	}

	public EntityNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public EntityNotFoundException(ErrorCode errorCode, String[] stringArgList) {
		super(errorCode, stringArgList);
	}
}