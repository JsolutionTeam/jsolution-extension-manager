package kr.co.jsol.jem.auth.application

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import kr.co.jsol.jem.user.domain.enums.TokenType
import kr.co.jsol.jem.user.infrastructure.dto.UserDetailsImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.security.Key
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@Component
class JwtService(
    private val userDetailsService: UserDetailsService,
    @Value("\${jwt.access-secret-key:}")
    strAccessSecretKey: String,
    @Value("\${jwt.refresh-secret-key:}")
    strRefreshSecretKey: String,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val userIdKey = "userId"

    private var accessSecretKey: Key =
        Keys.hmacShaKeyFor(strAccessSecretKey.ifBlank { UUID.randomUUID().toString() }.toByteArray())
    private var refreshSecretKey: Key =
        Keys.hmacShaKeyFor(strRefreshSecretKey.ifBlank { UUID.randomUUID().toString() }.toByteArray())
    private val accessJwtParser: JwtParser = Jwts.parserBuilder().setSigningKey(accessSecretKey).build()
    private val refreshJwtParser: JwtParser = Jwts.parserBuilder().setSigningKey(refreshSecretKey).build()

    fun getAuthentication(type: TokenType, token: String): Authentication {
        val jwtParser = when (type) {
            TokenType.ACCESS -> accessJwtParser
            TokenType.REFRESH -> refreshJwtParser
        }
        val claims = jwtParser.parseClaimsJws(token).body
        val userId = claims[userIdKey] as String
        val userDetails = userDetailsService.loadUserByUsername(userId) as UserDetailsImpl

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun validateToken(type: TokenType, token: String): Boolean {
        val jwtParser = when (type) {
            TokenType.ACCESS -> accessJwtParser
            TokenType.REFRESH -> refreshJwtParser
        }

        return runCatching {
            val claims = jwtParser.parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        }.onFailure {
            logger.debug(it.localizedMessage)
        }.getOrDefault(false)
    }

    fun createToken(type: TokenType, userId: String): String {
        val secretKey = when (type) {
            TokenType.ACCESS -> accessSecretKey
            TokenType.REFRESH -> refreshSecretKey
        }
        val expiredTime = when (type) {
            TokenType.ACCESS -> LocalDateTime.now().plusHours(9)
            TokenType.REFRESH -> LocalDateTime.now().plusDays(30)
        }

        val claims: Claims = Jwts.claims().setSubject(userId)
        claims[userIdKey] = userId
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(Date(expiredTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }
}
