package com.b1nd.dodamdodam.gateway.infrastructure.swagger.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.ArraySchema
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.IntegerSchema
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Dodam OpenAPI")
                    .description("도담도담 OpenAPI - 외부 InApp 서비스를 위한 공개 API")
                    .version("v1")
            )
            .components(
                Components()
                    .addSecuritySchemes("X-App-Id", SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .`in`(SecurityScheme.In.HEADER)
                        .name("X-App-Id")
                        .description("앱 Public ID (UUID)"))
                    .addSecuritySchemes("X-Api-Key", SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .`in`(SecurityScheme.In.HEADER)
                        .name("X-Api-Key")
                        .description("발급받은 API Key"))
                    .addSchemas("Response", responseSchema())
            )
            .addSecurityItem(
                SecurityRequirement()
                    .addList("X-App-Id")
                    .addList("X-Api-Key")
            )
            .path("/openapi/user/{publicId}", PathItem().get(
                operation("getUserByPublicId", "유저 단건 조회", "Public ID로 유저 정보를 조회합니다.")
                    .addParametersItem(
                        Parameter()
                            .name("publicId")
                            .`in`("path")
                            .required(true)
                            .schema(StringSchema().format("uuid"))
                            .description("조회할 유저의 Public ID")
                    )
            ))
            .path("/openapi/user", PathItem().post(
                operation("getUsersByPublicIds", "유저 다건 조회", "여러 유저의 Public ID로 유저 정보를 일괄 조회합니다.")
                    .requestBody(
                        RequestBody()
                            .required(true)
                            .content(Content().addMediaType("application/json",
                                MediaType().schema(
                                    ObjectSchema()
                                        .addProperty("userIds", ArraySchema()
                                            .items(StringSchema().format("uuid"))
                                            .description("조회할 유저 Public ID 목록"))
                                )))
                    )
            ))
    }

    private fun operation(operationId: String, summary: String, description: String): Operation {
        return Operation()
            .operationId(operationId)
            .summary(summary)
            .description(description)
            .responses(
                ApiResponses()
                    .addApiResponse("200", ApiResponse()
                        .description("성공")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/Response")))))
                    .addApiResponse("401", ApiResponse().description("인증 실패 - API Key가 없거나 유효하지 않음"))
                    .addApiResponse("403", ApiResponse().description("접근 거부 - API Key가 만료되었거나 권한 없음"))
            )
    }

    private fun responseSchema(): Schema<*> {
        return ObjectSchema()
            .addProperty("status", IntegerSchema().description("HTTP 상태 코드").example(200))
            .addProperty("message", StringSchema().description("응답 메시지"))
            .addProperty("data", ObjectSchema().description("응답 데이터"))
    }
}