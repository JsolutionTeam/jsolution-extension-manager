package kr.co.jsol.jem.user.infrastructure.repository

import kr.co.jsol.jem.user.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, String>
