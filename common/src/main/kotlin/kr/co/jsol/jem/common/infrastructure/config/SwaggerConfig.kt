package kr.co.jsol.jem.common.infrastructure.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
)
class SwaggerConfig {
    @Bean
    fun swagger(): OpenAPI = OpenAPI().info(
        Info()
            .title("Jsolution 외부 확장 프로그램 관리를 위한 프로젝트입니다. API Doc")
            .description("API 문서입니다.")
            .version("0.0.1-SNAPSHOT"),
    )


    @Bean
    fun apiV1(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("001. V1")
        .pathsToMatch("/api/v1/**")
        .displayName("API V1")
        .build()

    @Bean
    fun apiDefault(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("000. Default")
        .pathsToMatch("/api/**")
        .pathsToExclude("/api/v1/**")
        .displayName("Default API")
        .build()
}
