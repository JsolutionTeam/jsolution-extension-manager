package kr.co.jsol.jem.user.infrastructure.command

import kr.co.jsol.jem.common.infrastructure.repository.BaseCommandRepository
import kr.co.jsol.jem.user.domain.User
import kr.co.jsol.jem.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserCommandRepository(
    private val repository: UserRepository
) : BaseCommandRepository<User>(repository)
