package com.flab.mealmate.global.error;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.flab.mealmate.global.error.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"errorCode", "errorMessage", "fieldErrors"})
public class ErrorResponse {

	private ErrorCode errorCode;
	private String errorMessage;
	private List<FieldError> fieldErrors = new ArrayList<>();

	@Builder(builderMethodName = "errorMessageBuilder")
	public ErrorResponse(ErrorCode errorCode, String errorMessage, List<FieldError> fieldErrors) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.fieldErrors = fieldErrors;
	}

	public static ErrorResponse of(ErrorCode errorCode, String errorMessage, BindingResult bindingResult) {
		return ErrorResponse.errorMessageBuilder()
			.errorCode(errorCode)
			.errorMessage(errorMessage)
			.fieldErrors(FieldError.of(bindingResult))
			.build();
	}

	@Getter
	public static class FieldError {
		private String field;
		private String value;
		private String reason;

		private FieldError(final String field, final String value, final String reason) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}

		public static List<FieldError> of(final String field, final String value, final String reason) {
			List<FieldError> fieldErrors = new ArrayList<>();
			fieldErrors.add(new FieldError(field, value, reason));
			return fieldErrors;
		}

		private static List<FieldError> of(final BindingResult bindingResult) {
			final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
			return fieldErrors.stream()
				.map(error -> new FieldError(
					error.getField(),
					error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
					error.getDefaultMessage()))
				.collect(Collectors.toList());
		}
	}
}