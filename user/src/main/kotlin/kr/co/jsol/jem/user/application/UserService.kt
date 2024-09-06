package kr.co.jsol.jem.user.application

import kr.co.jsol.jem.common.domain.enums.UserAuthority
import kr.co.jsol.jem.common.infrastructure.exception.GeneralClientException
import kr.co.jsol.jem.common.infrastructure.exception.entities.UserException
import kr.co.jsol.jem.user.application.dto.GetUsersDto
import kr.co.jsol.jem.user.application.dto.SignUpDto
import kr.co.jsol.jem.user.application.dto.UpdateUserDto
import kr.co.jsol.jem.user.domain.User
import kr.co.jsol.jem.user.infrastructure.command.UserCommandRepository
import kr.co.jsol.jem.user.infrastructure.dto.UserDto
import kr.co.jsol.jem.user.infrastructure.query.UserQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val query: UserQueryRepository,
    private val command: UserCommandRepository,
    private val mapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
) {
    init {
        if (!query.existsAnyOne()) {
            command.save(
                User(
                    id = "jsolution",
                    authority = UserAuthority.ROOT,
                    name = "제이솔루션 관리자",
                    password = passwordEncoder.encode("jsolution@)@$12"),
                    email = "master@j-sol.co.kr",
                )
            )
        }
    }

    fun signUp(signUpDto: SignUpDto) {
        if (query.existsByEmail(signUpDto.id)) {
            throw UserException.AlreadyExistsException()
        }

        signUpDto.encodedPassword = passwordEncoder.encode(signUpDto.password)

        val user = mapper.signUpUser(signUpDto)
        command.save(user)
    }

    fun update(updateUserDto: UpdateUserDto): UserDto {
        val user = query.getById(updateUserDto.id!!)
        return UserDto(command.save(mapper.updateUser(user, updateUserDto)))
    }

    fun getUsers(id: String, pageable: Pageable, getUsersDto: GetUsersDto): Page<UserDto> {
        val user = query.getById(id)
        return when (user.authority) {
            UserAuthority.ROOT -> query.getWithPagination(pageable, getUsersDto).map { UserDto(it) }
            UserAuthority.MANAGER -> query.getWithPagination(pageable, getUsersDto, true).map { UserDto(it) }
            else -> throw GeneralClientException.ForbiddenException()
        }
    }

    fun getUser(id: String, targetUserId: String): UserDto {
        val user = query.getById(id)
        val targetUser = query.getById(targetUserId)
        when (user.authority) {
            UserAuthority.ROOT -> return UserDto(targetUser)
            UserAuthority.MANAGER -> {
                // MANAGER은 ROOT를 제외한 모든 사용자 정보를 조회할 수 있다.
                if (targetUser.authority == UserAuthority.ROOT) {
                    throw GeneralClientException.ForbiddenException()
                }
                return UserDto(targetUser)
            }

            else -> {
                // USER는 자신의 정보만 조회할 수 있다.
                if (user.id != targetUser.id) {
                    throw GeneralClientException.ForbiddenException()
                }
                return UserDto(targetUser)
            }
        }
    }
}
