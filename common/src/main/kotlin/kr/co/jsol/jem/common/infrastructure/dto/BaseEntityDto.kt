package kr.co.jsol.jem.common.infrastructure.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.jem.common.domain.BaseEntity
import kr.co.jsol.jem.common.infrastructure.annotation.JDateTimeFormat
import java.time.LocalDateTime

open class BaseEntityDto(baseEntity: BaseEntity) {
    val id: String = baseEntity.id

    @JDateTimeFormat
    @Schema(description = "생성일", required = false, example = "2021-08-01T00:00:00.000Z")
    val createdAt: LocalDateTime = baseEntity.createdAt

    @JDateTimeFormat
    @Schema(description = "수정일", required = false, example = "2021-08-01T00:00:00.000Z")
    val updatedAt: LocalDateTime? = baseEntity.updatedAt
}
