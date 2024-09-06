package kr.co.jsol.jem.auth.application.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "토큰 재발급 요청")
data class ReissueJwtDto(
    @field:Schema(description = "리프레시 토큰")
    val refreshToken: String,
)
