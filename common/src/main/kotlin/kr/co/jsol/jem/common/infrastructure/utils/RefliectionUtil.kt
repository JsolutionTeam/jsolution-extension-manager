package kr.co.jsol.jem.common.infrastructure.utils

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.lang.reflect.Field

fun Class<*>.getDeclaredField(name: String, lazyMessage: () -> Any): Field {
    return runCatching {
        this.getDeclaredField(name)
    }.onFailure {
        val message = lazyMessage()
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "$message $name")
    }.getOrThrow()
}
