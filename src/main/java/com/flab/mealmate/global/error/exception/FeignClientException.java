package com.flab.mealmate.global.error.exception;

import com.flab.mealmate.global.error.ErrorResponse;

public class FeignClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private int status;
    private ErrorResponse errorResponse;

    public FeignClientException(ErrorResponse errorData) {
        this.errorResponse = errorData;
    }

    public FeignClientException(int status, String resultMessage) {
        this.status = status;
        this.errorResponse = ErrorResponse.errorMessageBuilder()
            .errorCode(ErrorCode.ERR_FEIGN_CLIENT)
            .errorMessage(resultMessage)
            .build();
    }

    public int getStatus() {
        return status;
    }


    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
