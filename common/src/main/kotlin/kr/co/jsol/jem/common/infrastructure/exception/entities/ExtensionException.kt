package kr.co.jsol.jem.common.infrastructure.exception.entities

import kr.co.jsol.jem.common.infrastructure.exception.CustomException
import org.springframework.http.HttpStatus

object ExtensionException {
    private const val NAME = "확장프로그램"
    private const val CODE = "EXTENSION"

    class NotFoundByIdException @JvmOverloads constructor(
        message: String? = "id로 " + NAME + " 정보를 찾을 수 없습니다.",
        e: Throwable? = null
    ) :
        CustomException(
            CODE + "-0001",
            message ?: "id로 " + NAME + " 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, e
        )
}
