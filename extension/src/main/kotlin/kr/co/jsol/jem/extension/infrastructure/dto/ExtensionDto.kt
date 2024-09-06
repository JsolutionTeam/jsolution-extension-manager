package kr.co.jsol.jem.extension.infrastructure.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.jem.common.infrastructure.dto.BaseEntityDto
import kr.co.jsol.jem.extension.domain.Extension
import kr.co.jsol.jem.file.application.dto.FileDto

@Schema(name = "검사실 응답")
class ExtensionDto(extension: Extension) : BaseEntityDto(extension) {
    @field:Schema(description = "버전값 입력")
    val version: String = extension.version

    @field:Schema(description = "하위 폴더 (상대경로)")
    val folder: String = extension.folder

    @field:Schema(description = "파일 정보")
    val file: FileDto = FileDto(extension.file)
}
