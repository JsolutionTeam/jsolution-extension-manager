package kr.co.jsol.jem.common.infrastructure.annotation

import org.springframework.format.annotation.DateTimeFormat

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@DateTimeFormat(pattern = "HH:mm")
annotation class JTimeFormat
