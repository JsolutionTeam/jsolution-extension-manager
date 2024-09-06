package kr.co.jsol.jem.user.infrastructure.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.jem.common.domain.enums.UserAuthority
import kr.co.jsol.jem.common.infrastructure.dto.BaseEntityDto
import kr.co.jsol.jem.user.domain.User

class UserDto(@Schema(hidden = true) entity: User) : BaseEntityDto(entity) {
    val name: String = entity.name
    val email: String = entity.email
    val authority: UserAuthority = entity.authority
}
