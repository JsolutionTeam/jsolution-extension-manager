package kr.co.jsol.jem.user.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.jem.common.domain.enums.UserAuthority
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Schema(description = "회원가입")
data class DevSignUpDto(
    @field:NotBlank(message = "회사 아이디는 필수입니다.")
    @field:Size(max = 255, message = "회사 아이디는 255자 이하로 입력해주세요.")
    @field:Schema(description = "회사 아이디")
    val companyId: String,

    @field:NotBlank(message = "아이디는 필수입니다.")
    @field:Size(min = 4, max = 255, message = "아이디는 4자 이상 255자 이하로 입력해주세요.")
    @field:Schema(description = "아이디")
    val id: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    @field:Size(min = 4, max = 255, message = "비밀번호는 4자 이상 255자 이하로 입력해주세요.")
    @field:Schema(description = "비밀번호")
    val password: String,

    @field:NotBlank(message = "이름은 필수입니다.")
    @field:Size(max = 255, message = "이름은 255자 이하로 입력해주세요.")
    @field:Schema(description = "이름")
    val name: String,

    @field:NotBlank(message = "이메일은 필수입니다.")
    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    @field:Size(max = 255, message = "이메일은 255자 이하로 입력해주세요.")
    @field:Schema(description = "이메일")
    val email: String,

    @field:NotBlank(message = "연락처는 필수입니다.")
    @field:Size(max = 255, message = "연락처는 255 자 이하로 입력해주세요 .")
    @field:Schema(description = "연락처")
    val phone: String,

    @field:Size(max = 255, message = "주소는 255자 이하로 입력해주세요.")
    @field:Schema(description = "주소")
    val address: String?,

    @field:Size(max = 255, message = "상세주소는 255자 이하로 입력해주세요.")
    @field:Schema(description = "상세주소")
    val addressDetail: String?,

    @field:Size(max = 255, message = "우편번호는 255자 이하로 입력해주세요.")
    @field:Schema(description = "우편변호")
    val zipCode: String?,

    @field:NotNull(message = "권한은 필수입니다.")
    @field:Schema(description = "권한", implementation = UserAuthority::class)
    var authority: UserAuthority,
)
