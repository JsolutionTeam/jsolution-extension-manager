package kr.co.jsol.jem.auth.application

import kr.co.jsol.jem.user.infrastructure.dto.UserDetailsImpl
import kr.co.jsol.jem.user.infrastructure.query.UserQueryRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val query: UserQueryRepository,
) : UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val user = query.getById(id)
        return UserDetailsImpl(user)
    }
}
