package com.flab.mealmate.global.error;


import static com.flab.mealmate.global.error.exception.ErrorCode.*;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;


import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.DataBaseException;
import com.flab.mealmate.global.error.exception.ErrorCode;
import com.flab.mealmate.global.error.exception.FeignClientException;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final ReloadableResourceBundleMessageSource messageSource;

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		log.debug(null, e);
		var errorMessage = StringUtils.hasText(e.getMessage()) ? e.getMessage() : ERR_INVALID_TYPE_VALUE.getValue();
		var response = ErrorResponse.messageBuilder()
				.errorCode(ERR_INVALID_TYPE_VALUE)
				.errorMessage(errorMessage)
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}


	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
		log.debug(null, e);
		String errorMessage = messageSource.getMessage(e.getMessage(), e.getStringArgList(), LocaleContextHolder.getLocale());

		var errorCode = e.getErrorCode();
		var response = ErrorResponse.messageBuilder()
				.errorCode(errorCode)
				.errorMessage(errorMessage)
				.build();

		return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(FeignClientException.class)
	protected ResponseEntity<ErrorResponse> handleFeignClientException(FeignClientException e) {
		log.error(null, e);

		var response = e.getErrorResponse();

		return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler({MissingServletRequestParameterException.class})
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
		final MissingServletRequestParameterException e) {
		log.debug(null, e);

		var errorMessage = StringUtils.hasText(e.getMessage()) ? e.getMessage() : ERR_INVALID_INPUT_VALUE.getValue();
		var response = ErrorResponse.messageBuilder()
			.errorCode(ERR_INVALID_INPUT_VALUE)
			.errorMessage(errorMessage)
			.build();

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
	 * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
	 * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.debug(null, e);

		var errorMessage = StringUtils.hasText(e.getMessage()) ? e.getMessage() : ERR_INVALID_INPUT_VALUE.getValue();
		var response = ErrorResponse.messageBuilder()
			.errorCode(ERR_INVALID_INPUT_VALUE)
			.errorMessage(errorMessage)
			.build();

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
	 *                 ref
	 *                 https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
	 */
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		log.debug(null, e);

		var errorMessage = StringUtils.hasText(e.getMessage()) ? e.getMessage() : ERR_INVALID_INPUT_VALUE.getValue();
		var response = ErrorResponse.of(
			ERR_INVALID_INPUT_VALUE,
			errorMessage,
			e.getBindingResult());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * enum type 일치하지 않아 binding 못할 경우 발생
	 * 주로 @RequestParam enum으로 binding 못했을 경우 발생
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e) {
		log.debug(null, e);

		var errorMessage = StringUtils.hasText(e.getMessage()) ? e.getMessage() : ERR_INVALID_TYPE_VALUE.getValue();
		var response = ErrorResponse.messageBuilder()
			.errorCode(ERR_INVALID_TYPE_VALUE)
			.errorMessage(errorMessage)
			.build();

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * "multipart/form-data" 요청 시 필수 @RequestPart binding 못했을 경우 발생
	 */
	@ExceptionHandler({MissingServletRequestPartException.class, HttpMediaTypeNotSupportedException.class})
	protected ResponseEntity<ErrorResponse> handleServletException(ServletException e) {
		log.debug(null, e);

		var errorMessage = StringUtils.hasText(e.getMessage()) ? e.getMessage() : ERR_INVALID_INPUT_VALUE.getValue();
		var response = ErrorResponse.messageBuilder()
			.errorCode(ERR_INVALID_INPUT_VALUE)
			.errorMessage(errorMessage)
			.build();

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Request body 매핑 실패 시 발생
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.debug(null, e);

		if (e.getCause().getCause() instanceof BusinessException) {
			BusinessException businessException = (BusinessException) e.getCause();

			String errorMessage = messageSource.getMessage(businessException.getMessage(),
				businessException.getStringArgList(),
				LocaleContextHolder.getLocale());

			var response = ErrorResponse.messageBuilder()
				.errorCode(ERR_INVALID_TYPE_VALUE)
				.errorMessage(errorMessage)
				.build();

			return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
		}

		var response = ErrorResponse.messageBuilder()
			.errorCode(ERR_INVALID_TYPE_VALUE)
			.errorMessage(e.getMessage())
			.build();

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 지원하지 않은 HTTP method 호출 할 경우 발생
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e) {
		log.debug(null, e);

		var errorMessage = StringUtils.hasText(e.getMessage()) ? e.getMessage()
			: messageSource.getMessage(ERR_METHOD_NOT_ALLOWED.getValue(), null,
			LocaleContextHolder.getLocale());

		var response = ErrorResponse.messageBuilder()
			.errorCode(ERR_METHOD_NOT_ALLOWED)
			.errorMessage(errorMessage)
			.build();

		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * 500
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error(null, e);

		var errorMessage = StringUtils.hasText(e.getMessage()) ? e.getMessage()
			: messageSource.getMessage(ErrorCode.ERR_INTERNAL_SERVER_ERROR.getValue(), null,
			LocaleContextHolder.getLocale());

		var response = ErrorResponse.messageBuilder()
			.errorCode(ERR_INTERNAL_SERVER_ERROR)
			.errorMessage(errorMessage)
			.build();

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * DB 에러 처리
	 */
	@ExceptionHandler({DataBaseException.class})
	public ResponseEntity<ErrorResponse> handleDataBaseException(final DataBaseException e) {
		log.error(null, e);

		String message = messageSource.getMessage(ERR_DB.getValue(), null, LocaleContextHolder.getLocale());
		var response = ErrorResponse.messageBuilder()
			.errorCode(ERR_DB)
			.errorMessage(message)
			.build();

		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response);
	}


}
