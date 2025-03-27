package com.flab.mealmate.global.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flab.mealmate.global.error.ErrorResponse;

/**
 * JSON 유틸리티 클래스
 * LocalDate 및 LocalDateTime 직렬화/역직렬화 포맷 처리 및 ObjectMapper 제공
 */
public class JsonUtils {

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static SimpleModule createCustomTimeModule() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
			@Override
			public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeString(value.format(DATE_TIME_FORMATTER));
			}
		});
		module.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
			@Override
			public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeString(value.format(DATE_FORMATTER));
			}
		});
		module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			@Override
			public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				return LocalDateTime.parse(p.getValueAsString(), DATE_TIME_FORMATTER);
			}
		});
		module.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
			@Override
			public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				return LocalDate.parse(p.getValueAsString(), DATE_FORMATTER);
			}
		});
		return module;
	}

	public static final ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule())
		.registerModule(createCustomTimeModule())
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	public static ErrorResponse readErrorResponse(String str) {
		try {
			return objectMapper.readValue(str, ErrorResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}

