package kr.co.jsol.jem.common.infrastructure.utils

import kr.co.jsol.jem.common.infrastructure.dto.DateRangeDto
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return runCatching {
        ZonedDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"))
            .withZoneSameInstant(zoneId)
            .toLocalDateTime()
    }.onFailure {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "날짜 형식이 잘못되었습니다. (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')")
    }.getOrThrow()
}

fun LocalDateTime.toISOString(zoneId: ZoneId = ZoneId.systemDefault()): String {
    return this.atZone(zoneId).toInstant().toString()
}

fun LocalDateTime.toTimeStamp(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return this.atZone(zoneId).toInstant().toEpochMilli()
}

fun String.toDateRange(): DateRangeDto {
    val splitDateRange = this.split("~")
    if (splitDateRange.size != 2) {
        throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "날짜 범위 형식이 잘못되었습니다. (yyyy-MM-dd'T'HH:mm:ss.SSS'Z'~yyyy-MM-dd'T'HH:mm:ss.SSS'Z')",
        )
    }

    val dateRangeRegex = Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z$")
    splitDateRange.forEach {
        if (!dateRangeRegex.matches(it)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "날짜 범위 형식이 잘못되었습니다. (yyyy-MM-dd'T'HH:mm:ss.SSS'Z'~yyyy-MM-dd'T'HH:mm:ss.SSS'Z')",
            )
        }
    }
    return DateRangeDto(startedAt = splitDateRange[0], endedAt = splitDateRange[1])
}
