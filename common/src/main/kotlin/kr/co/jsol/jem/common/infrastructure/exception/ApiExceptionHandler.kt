package kr.co.jsol.jem.common.infrastructure.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.security.InvalidParameterException
import javax.validation.UnexpectedTypeException

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        val message = ex.bindingResult
            .allErrors[0]
            .defaultMessage ?: "요청 매개변수가 잘못되었습니다. 매개변수를 확인해주세요."
        log.error("handleMethodArgumentNotValidException - message : $message")
        return GeneralClientException.BadRequestException()
            .toEntity(message)
    }

    // @Valid 검증 실패 시 Catch
    @ExceptionHandler(InvalidParameterException::class)
    fun handleInvalidParameterException(ex: InvalidParameterException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("handleInvalidParameterException - message : $ex.message")
        return GeneralClientException.BadRequestException()
            .toEntity(ex.message)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("dataIntegrityViolationException - message : ${ex.message}")
        val message = "데이터 제약조건 오류가 발생했습니다. ${ex.message}"
        return GeneralClientException.BadRequestException()
            .toEntity(message)
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException::class)
    fun invalidDataAccessApiUsageException(
        ex: InvalidDataAccessApiUsageException,
    ): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("invalidDataAccessApiUsageException - message : ${ex.message}")
        return GeneralClientException.BadRequestException()
            .toEntity(ex.message)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentExceptionHandler(ex: IllegalArgumentException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("illegalArgumentExceptionHandler - message : ${ex.message}")
        return GeneralServerException.InternalServerException()
            .toEntity(ex.message)
    }

    /**
     * 요청 매개변수가 잘못되었을 경우
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun dtoTypeMissMatchException(ex: HttpMessageNotReadableException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()

        val msg = when (val causeException = ex.cause) {
            is InvalidFormatException -> {
                "입력 받은 ${causeException.value} 를 ${causeException.targetType} 으로 변환중 에러가 발생했습니다."
            }

            is MissingKotlinParameterException -> {
                "Parameter is missing: ${causeException.parameter.name}"
            }

            else -> "요청을 역직렬화 하는과정에서 예외가 발생했습니다."
        }

        return GeneralClientException.BadRequestException()
            .toEntity(msg)
    }

    /**
     * 요청 매개변수가 잘못되었을 경우
     * ex) String을 String? 타입처럼 null을 보냈을 경우 BindingException 발생
     */
    @ExceptionHandler(BindException::class)
    fun beanPropertyBindingResult(ex: BindException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        val codes = ex.bindingResult.fieldErrors.map {
            log.error("filedError : ${it.field}, ${it.defaultMessage}")
            "${it.field} : ${it.defaultMessage}"
        }
            .toMutableList()

        val message = "요청 매개변수가 잘못되었습니다. 매개변수를 확인해주세요. 상세 정보 : ${codes.joinToString(",")}"

        return GeneralClientException.BadRequestException()
            .toEntity(message)
    }

    /**
     * 토큰 바디가 존재 하지 않을 경우
     */
    @ExceptionHandler(ServletRequestBindingException::class)
    fun servletRequestBindingException(ex: ServletRequestBindingException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("ServletRequestBindingException - message : ${ex.message}")
        log.error("ServletRequestBindingException - stackTrace : ${ex.stackTraceToString()}")
        val message = "요청 매개변수가 잘못되었습니다. 매개변수를 확인해주세요."
        return GeneralClientException.BadRequestException()
            .toEntity(message)
    }

    /**
     *  ValidationException
     */
    @ExceptionHandler(UnexpectedTypeException::class)
    fun unexpectedTypeException(ex: UnexpectedTypeException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("UnexpectedTypeException - message : ${ex.message}")
        return GeneralServerException.InternalServerException()
            .toEntity(ex.message)
    }

    /**
     * 토큰이 유효하지 않을 경우
     */
    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(ex: Exception): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        return GeneralClientException.BadRequestException()
            .toEntity("토큰이 유효하지 않습니다.")
    }

    /**
     * NullPointException 예외 처리 메서드입니다.
     */
    @ExceptionHandler(NullPointerException::class)
    fun handleNullPointException(ex: Exception): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("NullPointerException - message : ${ex.message}")
        return GeneralServerException
            .ServerNullPointerException()
            .toEntity()
    }

    /**
     * 사용자 권한 부족으로 인한 접근 권한 거부
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun springSecurityAccessDeniedException(ex: Exception): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("Spring Security - AccessDeniedException - message : ${ex.message}")
        return GeneralClientException.ForbiddenException()
            .toEntity(ex.message) // ex.message = 접근이 거부되었습니다
    }

    /**
     * 실 존재하는 파일 접근 권한 부족으로 인한 접근 권한 거부
     */
    @ExceptionHandler(AccessDeniedException::class)
    fun fileAccessDeniedException(ex: Exception): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("FileSystemException -  AccessDeniedException - message : ${ex.message}")
        val message = "서버에서 요청한 파일을 찾아올 수 없었습니다. 담당 개발자에게 연락바랍니다."
        return GeneralServerException.InternalServerException()
            .toEntity(message)
    }

    /**
     * 요청 핸들러에서 해당하는 요청 메서드가 없을 경우 발생하는 예외 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(ex: Exception): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("HttpRequestMethodNotSupportedException - message : ${ex.message}")
        val message = "${ex.localizedMessage} 요청 메서드를 확인해 주세요"
        return GeneralClientException.BadRequestException()
            .toEntity(message)
    }

    /**
     * NoSuchElementException 예외 처리 메서드입니다.
     * @param ex 예외 객체
     * @return ResponseEntity<CustomException.ExceptionBody티
     */
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("NoSuchElementException - localizedMessage: ${ex.localizedMessage}")
        val message = "요청이 올바르지 않습니다. \n${ex.localizedMessage}"
        return GeneralClientException.BadRequestException()
            .toEntity(message)
    }

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("CustomException - message : ${ex.message}")
        ex.let {
            log.error(it.message, it.throwable)
            return it.toEntity()
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<CustomException.ExceptionBody> {
        ex.printStackTrace()
        log.error("kotlin.Exception - message : ${ex.localizedMessage}")
        ex.let {
            log.error(it.message, it)
            return CustomException("000", it.message.toString(), HttpStatus.INTERNAL_SERVER_ERROR).toEntity()
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
