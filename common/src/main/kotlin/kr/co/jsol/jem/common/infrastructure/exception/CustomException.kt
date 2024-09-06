package kr.co.jsol.jem.common.infrastructure.exception

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

open class CustomException(
    val code: String,
    override val message: String,
    val status: HttpStatus,
    val throwable: Throwable? = null,
) : RuntimeException() {

    protected constructor(
        exception: CustomException,
        throwable: Throwable?,
    ) : this(
        code = exception.code,
        message = exception.message,
        status = exception.status,
        throwable = throwable,
    )

    fun toEntity(overrideMessage: String? = null): ResponseEntity<ExceptionBody> =
        ResponseEntity.status(status)
            .body(ExceptionBody(code = code, message = overrideMessage ?: message, status = status.value()))

    @Schema(description = "기본 에러")
    data class ExceptionBody(
        @field:Schema(description = "에러 코드")
        val code: String,
        @field:Schema(description = "에러 메시지")
        val message: String,
        @field:Schema(description = "에러 상태 코드")
        val status: Int,
    )
}
