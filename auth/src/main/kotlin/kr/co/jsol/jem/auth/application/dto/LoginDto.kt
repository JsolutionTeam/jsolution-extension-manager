package kr.co.jsol.jem.auth.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Schema(name = "로그인 요청", description = "로그인")
data class LoginDto(
    @field:NotBlank(message = "아이디는 필수입니다.")
    @field:Size(min = 4, max = 255, message = "아이디는 4자 이상 255자 이하로 입력해주세요.")
    @field:Schema(description = "아이디", example = "string")
    val id: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    @field:Size(min = 4, max = 255, message = "비밀번호는 4자 이상 255자 이하로 입력해주세요.")
    @field:Schema(description = "비밀번호", example = "string")
    val password: String,
)
