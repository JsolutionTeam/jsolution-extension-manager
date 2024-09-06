package kr.co.jsol.jem.common.infrastructure.utils

fun requireNotEmpty(value: String?, lazyMessage: () -> Any): String {
    if (value.isNullOrEmpty()) {
        val message = lazyMessage()
        throw IllegalArgumentException(message.toString())
    }
    return value
}

fun <T : Any?> requireNull(value: T, lazyMessage: () -> Any) {
    if (value != null) {
        val message = lazyMessage()
        throw IllegalArgumentException(message.toString())
    }
}
