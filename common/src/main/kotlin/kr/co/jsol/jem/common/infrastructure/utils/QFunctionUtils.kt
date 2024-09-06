package kr.co.jsol.jem.common.infrastructure.utils

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringExpression
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.core.types.dsl.StringTemplate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.format.annotation.DateTimeFormat.ISO.NONE
import org.springframework.format.annotation.DateTimeFormat.ISO.TIME

fun distinct(str: StringPath): StringTemplate = Expressions.stringTemplate("distinct {0}", str)

/**
 * Mariadb의 group_concat 함수를 사용하기 위해 설정
 */
fun groupConcat(str: StringPath): StringTemplate = Expressions.stringTemplate("group_concat({0})", str)

/**
 * DATE_FORMAT 함수를 사용하기 위해 설정
 */
fun toDateString(str: StringExpression, format: String = "%Y-%m-%d"): StringTemplate =
    Expressions.stringTemplate("DATE_FORMAT({0}, {1})", str, format)

/**
 * DATE_FORMAT 함수를 사용하기 위해 설정
 */
fun toTimeString(str: StringExpression, format: String = "%H:%i:%s"): StringTemplate =
    Expressions.stringTemplate("DATE_FORMAT({0}, {1})", str, format)


/**
 * DATE_FORMAT 함수를 사용하기 위해 설정
 */
fun toTimeString(str: StringExpression, format: DateTimeFormat.ISO): StringTemplate {
    val dateFormat = when (format) {
        TIME -> "%H:%i:%s"
        DATE -> "%Y-%m-%d"
        DATE_TIME -> "%Y-%m-%d %H:%i:%s"
        NONE -> "%H:%i:%s" // default
    }
    return Expressions.stringTemplate("DATE_FORMAT({0}, {1})", str, dateFormat)
}

/**
 * INSTR 함수로 문자열이 포함되어 있는지 확인
 */
fun strContains(str: StringPath, value: String): BooleanExpression =
    Expressions.booleanTemplate("INSTR({0}, {1}) > 0", str, value)
