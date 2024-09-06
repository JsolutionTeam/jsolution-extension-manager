package kr.co.jsol.jem.auth.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.jsol.jem.auth.application.AuthService
import kr.co.jsol.jem.auth.application.dto.LoginDto
import kr.co.jsol.jem.auth.application.dto.ReissueJwtDto
import kr.co.jsol.jem.auth.infrastructure.dto.JwtToken
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "000. 권한", description = "권한 관리 API")
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val service: AuthService,
) {
    @Operation(summary = "토큰 재발급 (ROOT, MANAGER, USER, NONE)")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "유효하지 않은 토큰입니다.", content = [Content()]),
    )
    @PostMapping("/refresh")
    @ResponseStatus(value = HttpStatus.OK)
    fun refreshToken(
        @RequestBody
        reissueJwtDto: ReissueJwtDto,
    ): JwtToken {
        log.info("[AuthController - refreshToken] reissueJwtDto: $reissueJwtDto")
        return service.refreshToken(reissueJwtDto)
    }

    @Operation(summary = "로그인")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "로그인 실패하셨습니다.", content = [Content()]),
    )
    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    fun login(@RequestBody loginDto: LoginDto): JwtToken {
        log.info("[AuthController - login] loginDto: $loginDto")
        return service.login(loginDto)
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
