package kr.co.jsol.jem.auth.application

import kr.co.jsol.jem.auth.application.dto.LoginDto
import kr.co.jsol.jem.auth.application.dto.ReissueJwtDto
import kr.co.jsol.jem.auth.infrastructure.dto.JwtToken
import kr.co.jsol.jem.common.infrastructure.exception.GeneralClientException
import kr.co.jsol.jem.common.infrastructure.exception.entities.UserException
import kr.co.jsol.jem.user.domain.enums.TokenType
import kr.co.jsol.jem.user.infrastructure.dto.UserDetailsImpl
import kr.co.jsol.jem.user.infrastructure.query.UserQueryRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userQuery: UserQueryRepository,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun refreshToken(reissueJwtDto: ReissueJwtDto): JwtToken {
        logger.info("[AuthService - refreshToken] reissueJwtDto: $reissueJwtDto")
        val user: UserDetailsImpl = try {
            val refreshToken = reissueJwtDto.refreshToken
            jwtService.validateToken(TokenType.REFRESH, refreshToken)

            jwtService.getAuthentication(TokenType.REFRESH, refreshToken).principal as UserDetailsImpl
        } catch (e: Exception) {
            throw GeneralClientException.InvalidTokenException(e = e)
        }
        return jwtToken(user.username)
    }

    fun login(loginDto: LoginDto): JwtToken {
        logger.info("[AuthService - login] id: ${loginDto.id}")
        val user = userQuery.findById(loginDto.id) ?: throw GeneralClientException.LoginFailedException()

        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(user.id, loginDto.password))
        } catch (e: AuthenticationException) {
            logger.debug(e.localizedMessage)
            throw GeneralClientException.LoginFailedException()
        }

        // 1.2. 만약 비활성화 되어있다면 로그인 불가
        if (!user.enabled) {
            throw UserException.DisabledUserException()
        }

        // 2. 회사 사용 가능 여부를 체크한다.
        return jwtToken(user.id)
    }

    private fun jwtToken(userId: String) = JwtToken(
        accessToken = jwtService.createToken(TokenType.ACCESS, userId),
        refreshToken = jwtService.createToken(TokenType.REFRESH, userId),
    )
}
