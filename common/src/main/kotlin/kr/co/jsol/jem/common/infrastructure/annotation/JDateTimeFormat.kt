package kr.co.jsol.jem.common.infrastructure.annotation

import org.springframework.format.annotation.DateTimeFormat

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
annotation class JDateTimeFormat
