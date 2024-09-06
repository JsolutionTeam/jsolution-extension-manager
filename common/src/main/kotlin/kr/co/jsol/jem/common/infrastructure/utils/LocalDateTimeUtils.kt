package kr.co.jsol.jem.common.infrastructure.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.removeTime(): LocalDateTime {
    return this.withHour(0).withMinute(0).withSecond(0).withNano(0)
}

// LocalDate의 23:59:59를 반환
fun LocalDate.atEndOfDay(): java.time.LocalDateTime =
    this.atTime(23, 59, 59)

fun LocalTime.toFormatString(format: String = "HH:mm:ss"): String {
    return this.format(DateTimeFormatter.ofPattern(format))
}
