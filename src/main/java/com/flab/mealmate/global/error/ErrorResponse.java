package com.flab.mealmate.global.error;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ErrorResponse {

	private ErrorData errorData;

	private ErrorResponse(ErrorData errorData) {
		this.errorData = errorData;
	}

	public static ErrorResponse of(final ErrorData errorData) {
		return new ErrorResponse(errorData);
	}
	public static ErrorResponse of(final String errorData, final BindingResult bindingResult) {
		return new ErrorResponse(ErrorData.fieldErrorsBuilder()
			.errorMessage(errorData)
			.fieldErrors(FieldError.of(bindingResult))
			.build());
	}

	public static ErrorResponse of(String resultMessage, MethodArgumentTypeMismatchException e) {
		final String value = e.getValue() == null ? resultMessage : e.getValue().toString();
		final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
		ErrorData errorData = ErrorData.fieldErrorsBuilder()
			.fieldErrors(errors)
			.build();
		return new ErrorResponse(errorData);
	}


	@Getter
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({"errorMessage", "fieldErrors"})
	public static class ErrorData {

		@JsonProperty("errorMessage")
		private String errorMessage;

		private List<FieldError> fieldErrors;

		@Builder(builderMethodName = "messageBuilder", builderClassName = "MessageBuilderClass")
		public ErrorData(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		@Builder(builderMethodName = "fieldErrorsBuilder", builderClassName = "FieldErrorsBuilderClass")
		public ErrorData(String errorMessage, List<FieldError> fieldErrors) {
			this.errorMessage = errorMessage;
			this.fieldErrors = fieldErrors;
		}
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