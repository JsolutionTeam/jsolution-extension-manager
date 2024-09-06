package kr.co.jsol.jem.user.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.api.annotations.ParameterObject

@ParameterObject
@Schema(name = "사용자 조회")
data class GetUsersDto(
    @field:Schema(description = "아이디")
    val id: String?,

    @field:Schema(description = "이름")
    val name: String?,

    @field:Schema(description = "생성일시 시작범위", example = "2021-08-01T00:00:00.000Z")
    val startCreatedAt: String?,

    @field:Schema(description = "생성일시 종료범위", example = "2021-08-01T00:00:00.000Z")
    val endCreatedAt: String?,

    @field:Schema(description = "잔여포인트 시작범위")
    val startRemainingPoint: Int?,

    @field:Schema(description = "잔여포인트 종료범위")
    val endRemainingPoint: Int?,

    @field:Schema(description = "참여펀딩개수 시작범위")
    val startParticipatedFundingCount: Int?,

    @field:Schema(description = "참여펀딩개수 종료범위")
    val endParticipatedFundingCount: Int?,

    @field:Schema(description = "신청펀딩개수 시작범위")
    val startAppliedFundingCount: Int?,

    @field:Schema(description = "신청펑딩개수 종료범위")
    val endAppliedFundingCount: Int?,
)
