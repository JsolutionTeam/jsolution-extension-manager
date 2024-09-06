package kr.co.jsol.jem.file.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.jem.file.domain.File

@Schema(name = "파일 응답")
data class FileDto(
    @field:Schema(description = "원본 파일명")
    val originalName: String,

    @field:Schema(description = "저장 파일명, 기본은 random UUID")
    val name: String,

    @field:Schema(description = "파일 확장자")
    val extension: String,

    @field:Schema(description = "파일 다운로드 경로(해당 경로로 파일 다운로드 가능)")
    val downloadUrl: String,

    @field:Schema(description = "파일 크기")
    val size: Long,
) {
    constructor(file: File) : this(
        file.originalName,
        file.name,
        file.extension,
        file.downloadUrl,
        file.size
    )
}
