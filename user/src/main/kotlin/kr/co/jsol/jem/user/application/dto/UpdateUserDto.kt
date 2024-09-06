package kr.co.jsol.jem.user.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Schema(description = "유저 정보 수정")
data class UpdateUserDto(
    @field:Schema(description = "아이디, ROOT, MANAGER 권한만 지정가능하며 USER는 자신의 id로 강제 적용")
    var id: String?,

    @field:Size(max = 20, message = "이름은 20자 이하로 입력해주세요.")
    @field:Schema(description = "이름")
    val name: String?,

    @field:Size(max = 255, message = "이메일은 255자 이하로 입력해주세요.")
    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    @field:Schema(description = "이메일")
    val email: String?,
)
