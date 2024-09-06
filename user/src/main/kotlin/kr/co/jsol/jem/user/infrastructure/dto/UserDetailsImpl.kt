package kr.co.jsol.jem.user.infrastructure.dto

import kr.co.jsol.jem.common.domain.enums.UserAuthority
import kr.co.jsol.jem.user.domain.User
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    private val user: User,
) : UserDetails {

    val isRoot: Boolean = user.authority == UserAuthority.ROOT
    val isMANAGER: Boolean = user.authority == UserAuthority.MANAGER
    val isUser: Boolean = user.authority == UserAuthority.USER
    val realName: String = user.name

    override fun getAuthorities(): MutableCollection<out UserAuthority> =
        mutableListOf(user.authority)

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.id

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
