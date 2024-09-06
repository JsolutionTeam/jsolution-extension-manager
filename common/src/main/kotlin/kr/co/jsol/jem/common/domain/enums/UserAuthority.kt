package kr.co.jsol.jem.common.domain.enums

import org.springframework.security.core.GrantedAuthority

enum class UserAuthority(private val authority: String, val description: String) : GrantedAuthority {
    ROOT(Roles.ROOT, "최상위권한"),
    MANAGER(Roles.MANAGER, "관리자권한"),
    USER(Roles.USER, "유저권한"),
    ;

    object Roles {
        const val ROOT = "ROOT"
        const val MANAGER = "MANAGER"
        const val USER = "USER"
    }

    object RoleCheckMethod {
        const val HasRootRole = "hasAnyAuthority(\"ROOT\")"
        const val HasRootAndManagerRole = "hasAnyAuthority(\"ROOT\", \"MANAGER\")"
        const val HasManagerRole = "hasAnyAuthority(\"MANAGER\")"
        const val HasManagerAndUserRole = "hasAnyAuthority(\"MANAGER\", \"USER\")"
        const val HasUserRole = "hasAnyAuthority(\"USER\")"
        const val HasAnyRole = "hasAnyAuthority(\"ROOT\", \"MANAGER\", \"USER\")"
    }

    override fun getAuthority(): String {
        return authority
    }
}
