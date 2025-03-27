package com.flab.mealmate.global.error.exception;

import com.flab.mealmate.global.error.ErrorResponse;

public class FeignClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private int status;
    private ErrorResponse.ErrorData errorData;

    public FeignClientException(ErrorResponse.ErrorData errorData) {
        this.errorData = errorData;
    }

    public FeignClientException(int status, String resultMessage) {
        this.status = status;
        this.errorData = ErrorResponse.ErrorData.messageBuilder()
            .errorMessage(resultMessage)
            .build();
    }

    public int getStatus() {
        return status;
    }


    public ErrorResponse.ErrorData getErrorData() {
        return errorData;
    }
}
