package com.flab.mealmate.global;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import java.util.List;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Snippet;

import com.epages.restdocs.apispec.HeaderDescriptorWithType;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;

public final class ApiDocumentation {

	// @Deprecated(forRemoval = true)
	public static RestDocumentationResultHandler document(String identifier, Snippet snippets) {
		return MockMvcRestDocumentationWrapper.document(identifier,
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				snippets);
	}

	public static HeaderDescriptorWithType header(String name, SimpleType type, String description) {
		return headerWithName(name).type(type).description(description);
	}

	public static ParameterDescriptorWithType parameter(String name, SimpleType type, String description) {
		return parameterWithName(name).type(type).description(description);
	}

	public static ParameterDescriptorWithType pathParameter(String name, SimpleType type, String description) {
		return parameter(name, type, description);
	}

	public static FieldDescriptor field(String path, JsonFieldType type, String description) {
		return PayloadDocumentation.fieldWithPath(path).type(type).description(description);
	}

	public static FieldDescriptor subsectionField(String path, JsonFieldType type, String description) {
		return PayloadDocumentation.subsectionWithPath(path).type(type).description(description);
	}

	public static ApiDocumentationBuilder builder() {
		return new ApiDocumentationBuilder();
	}

	public static class ApiDocumentationBuilder {

		private String tag;
		private String description;
		private Schema requestSchema;
		private Schema responseSchema;
		private List<HeaderDescriptorWithType> requestHeaders = List.of();
		private List<HeaderDescriptorWithType> responseHeaders = List.of();
		private List<ParameterDescriptorWithType> pathParameters = List.of();
		private List<ParameterDescriptorWithType> requestParameters = List.of();
		private List<FieldDescriptor> requestFields = List.of();
		private List<FieldDescriptor> responseFields = List.of();

		ApiDocumentationBuilder() {
		}

		public ApiDocumentationBuilder tag(String tag) {
			this.tag = tag;
			return this;
		}

		public ApiDocumentationBuilder description(String description) {
			this.description = description;
			return this;
		}

		public ApiDocumentationBuilder requestSchema(String requestSchema) {
			this.requestSchema = Schema.schema(requestSchema);
			return this;
		}

		public ApiDocumentationBuilder responseSchema(String responseSchema) {
			this.responseSchema = Schema.schema(responseSchema);
			return this;
		}

		public ApiDocumentationBuilder requestHeaders(HeaderDescriptorWithType... requestHeaders) {
			this.requestHeaders = List.of(requestHeaders);
			return this;
		}

		public ApiDocumentationBuilder responseHeaders(HeaderDescriptorWithType... responseHeaders) {
			this.responseHeaders = List.of(responseHeaders);
			return this;
		}

		public ApiDocumentationBuilder pathParameters(ParameterDescriptorWithType... pathParameters) {
			this.pathParameters = List.of(pathParameters);
			return this;
		}

		public ApiDocumentationBuilder requestParameters(ParameterDescriptorWithType... requestParameters) {
			this.requestParameters = List.of(requestParameters);
			return this;
		}

		public ApiDocumentationBuilder requestFields(FieldDescriptor... requestFields) {
			this.requestFields = List.of(requestFields);
			return this;
		}

		public ApiDocumentationBuilder responseFields(FieldDescriptor... responseFields) {
			this.responseFields = List.of(responseFields);
			return this;
		}

		public RestDocumentationResultHandler build() {
			return MockMvcRestDocumentationWrapper.document("{class-name}/{method-name}",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					resource(ResourceSnippetParameters.builder()
							.tag(tag)
							.description(description)
							.requestSchema(requestSchema)
							.responseSchema(responseSchema)
							.requestHeaders(requestHeaders)
							.responseHeaders(responseHeaders)
							.pathParameters(pathParameters)
							.queryParameters(requestParameters)
							.requestFields(requestFields)
							.responseFields(responseFields)
							.build()));
		}

	}

}
