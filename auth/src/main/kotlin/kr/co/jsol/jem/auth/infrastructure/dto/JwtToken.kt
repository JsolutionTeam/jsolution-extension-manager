package kr.co.jsol.jem.auth.infrastructure.dto

data class JwtToken(
    val refreshToken: String,
    val accessToken: String
)
