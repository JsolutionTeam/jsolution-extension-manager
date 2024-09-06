package kr.co.jsol.jem.file.application.dto

import com.sun.istack.NotNull
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.multipart.MultipartFile

data class FileUploadDto(
    @field:NotNull
    @field:Parameter(description = "업로드할 파일")
    val file: MultipartFile,

    @field:Parameter(description = "파일 닉네임")
    val fileNickname: String? = null,

    @field:Parameter(description = "가로 사이즈")
    val widthSize: Int? = null,

    @field:Parameter(description = "세로 사이즈")
    val heightSize: Int? = null
)
