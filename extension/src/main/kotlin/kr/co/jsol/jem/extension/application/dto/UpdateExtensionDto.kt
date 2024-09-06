package kr.co.jsol.jem.extension.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotNull


@Schema(description = "확장 프로그램 업데이트 DTO")
data class UpdateExtensionDto(
    @field:Schema(description = "확장 프로그램 아이디")
    val id: String,

    @field:Schema(description = "버전")
    val version: String,

    @field:Schema(description = "폴더")
    val folder: String,

    @field:NotNull(message = "파일은 필수입니다.")
    @field:Schema(description = "파일")
    val file: MultipartFile,
)
