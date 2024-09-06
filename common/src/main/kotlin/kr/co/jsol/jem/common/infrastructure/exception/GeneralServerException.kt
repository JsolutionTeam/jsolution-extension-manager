package kr.co.jsol.jem.common.infrastructure.exception

import org.springframework.http.HttpStatus

sealed class GeneralServerException {
    class InternalServerException(
        message: String? = null,
        e: Throwable? = null,
    ) : CustomException("GNR-5000", message ?: "서버 에러 발생, 담당 개발자에게 연락해주세요.", HttpStatus.INTERNAL_SERVER_ERROR, e)

    class ServerNullPointerException(
        message: String? = null,
        e: Throwable? = null,
    ) : CustomException("GNR-5001", message ?: "서버 에러 발생, 담당 개발자에게 연락해주세요.", HttpStatus.INTERNAL_SERVER_ERROR, e)

    /**
     * 파일 시스템 관리 중 에러 발생
     */
    class ManageSystemFileException(
        message: String = "시스템 파일 관리 중 오류가 발생했습니다",
        e: Throwable? = null,
    ) : CustomException("GNR-FILE-0001", message, HttpStatus.BAD_REQUEST, e)

}
