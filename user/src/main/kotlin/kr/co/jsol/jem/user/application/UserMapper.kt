package kr.co.jsol.jem.user.application

import kr.co.jsol.jem.user.application.dto.SignUpDto
import kr.co.jsol.jem.user.application.dto.UpdateUserDto
import kr.co.jsol.jem.user.domain.User
import org.springframework.stereotype.Service

@Service
class UserMapper {
    fun signUpUser(signUpDto: SignUpDto): User {
        return User(
            id = signUpDto.id,
            name = signUpDto.name,
            email = signUpDto.email,
            password = signUpDto.encodedPassword,
            authority = signUpDto.authority
        )
    }

    fun updateUser(user: User, updateUserDto: UpdateUserDto): User {
        updateUserDto.name?.let { user.name = it }
        updateUserDto.email?.let { user.email = it }
        return user
    }
}
