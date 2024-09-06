package kr.co.jsol.jem.common.infrastructure.exception.entities

import kr.co.jsol.jem.common.infrastructure.exception.CustomException
import org.springframework.http.HttpStatus

sealed class UserException {
    class NotFoundByIdException(
        message: String = "id로 $NAME 정보를 찾을 수 없습니다.",
        e: Throwable? = null,
    ) : CustomException("$CODE-0001", message, HttpStatus.NOT_FOUND, e)

    class AlreadyExistsException(
        message: String = "이미 회원가입된 이메일입니다.",
        e: Throwable? = null,
    ) : CustomException("$CODE-0002", message, HttpStatus.BAD_REQUEST)

    // enabled가 false인 경우
    class DisabledUserException(
        message: String = "사용 중지된 사용자입니다.",
        e: Throwable? = null,
    ) : CustomException("$CODE-0003", message, HttpStatus.FORBIDDEN)

    companion object {
        private const val NAME = "사용자"
        private const val CODE = "USER"
    }
}
