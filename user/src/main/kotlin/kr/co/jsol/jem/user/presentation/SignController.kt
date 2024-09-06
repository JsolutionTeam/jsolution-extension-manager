package kr.co.jsol.jem.user.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.jsol.jem.common.domain.enums.UserAuthority
import kr.co.jsol.jem.user.application.UserService
import kr.co.jsol.jem.user.application.dto.SignUpDto
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Tag(name = "001. 사용자", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
class SignController(
    private val service: UserService,
) {
    @Operation(summary = "회원가입 처리 (ROOT 계정만 가능)")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "데이터가 유효하지 않습니다.", content = [Content()]),
    )
    @PreAuthorize(UserAuthority.RoleCheckMethod.HasRootRole)
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    fun signUp(
        @Valid @RequestBody
        signUpDto: SignUpDto
    ) {
        return service.signUp(signUpDto)
    }
}
