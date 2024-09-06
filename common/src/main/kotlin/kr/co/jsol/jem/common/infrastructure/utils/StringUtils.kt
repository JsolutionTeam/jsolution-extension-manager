package kr.co.jsol.jem.common.infrastructure.utils

import java.util.UUID

/**
 * string pascal case to camel case
 * ex : PascalCase -> pascalCase
 */
fun String.pascalToCamel(): String {
    return if (isNotEmpty()) {
        this[0].lowercaseChar() + substring(1)
    } else {
        this
    }
}

/**
 * String?.isNullOrBlank().not() function
 */
fun String?.isNotNullOrBlank(): Boolean {
    return !this.isNullOrBlank()
}

/**
 *  if (createProductDto.qrBase.isNullOrBlank()) {
 *                 UUID.randomUUID().toString()
 *             } else {
 *                 createProductDto.qrBase
 *             },
 *             위와 같은 형식을 infix function 으로 사용할 수 있도록 함
 * @example
 */
fun String?.orRandomUUID(): String {
    return if (this.isNullOrBlank()) {
        UUID.randomUUID().toString()
    } else {
        this
    }
}
