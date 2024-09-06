package kr.co.jsol.jem.extension.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Size

@Schema(name = "확장 파일 등록")
class GetExtensionsDto {
    @field:Size(max = 255, message = "키 값은 255자 이하로 입력해주세요.")
    @field:Schema(description = "키 값 입력")
    val id: String? = null

    @field:Size(max = 255, message = "버전은 255자 이하로 입력해주세요.")
    @field:Schema(description = "버전")
    val version: String? = null

    @field:Size(max = 255, message = "하위 폴더는 255자 이하로 입력해주세요.")
    @field:Schema(description = "하위 폴더 (상대경로)")
    val folder: String? = null

    val file: File? = null

    @Schema(name = "파일 정보")
    class File(
        @field:Size(max = 255, message = "원본 파일명은 255자 이하로 입력해주세요.")
        @field:Schema(description = "원본 파일명")
        val originalName: String? = null,

        @field:Size(max = 255, message = "저장된 파일명은 255자 이하로 입력해주세요.")
        @field:Schema(description = "저장된 파일명, 기본은 random UUID")
        val name: String? = null,

        @field:Size(max = 255, message = "확장자는 255자 이하로 입력해주세요.")
        @field:Schema(description = "확장자")
        val extension: String? = null,
    )
}
