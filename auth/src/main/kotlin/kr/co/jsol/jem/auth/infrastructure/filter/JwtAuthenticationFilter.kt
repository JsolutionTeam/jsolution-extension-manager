package kr.co.jsol.jem.auth.infrastructure.filter

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.jsol.jem.auth.application.JwtService
import kr.co.jsol.jem.user.domain.enums.TokenType
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(private val jwtService: JwtService) : GenericFilterBean() {

    private val objectMapper = ObjectMapper()

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse
        res.setHeader("Access-Control-Allow-Origin", "*")
        res.setHeader("Access-Control-Allow-Methods", "*")
        res.setHeader("Access-Control-Max-Age", "3600")
        res.setHeader(
            "Access-Control-Allow-Headers",
            "Origin, Content-Type, Accept, Authorization",
        )

        val rawToken = request.getHeader("Authorization")
        if (rawToken == null || rawToken.indexOf(BEARER_PREFIX) < 0) {
            chain.doFilter(request, response)
            return
        }
        val token = rawToken.replace(BEARER_PREFIX, "")
        val tokenType = if (req.requestURL.contains("auth/refresh")) TokenType.REFRESH else TokenType.ACCESS
        if (!jwtService.validateToken(tokenType, token)) {
            responseMessage(res, objectMapper, "토큰이 유효하지 않습니다.")
            SecurityContextHolder.clearContext()
            return
        }

        val authentication = jwtService.getAuthentication(tokenType, token)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    @Throws(IOException::class)
    private fun responseMessage(response: HttpServletResponse, objectMapper: ObjectMapper, message: String) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.characterEncoding = "UTF-8"
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        objectMapper.writeValue(response.outputStream, message)
    }

    companion object {
        const val BEARER_PREFIX = "Bearer "
    }
}
