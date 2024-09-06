package kr.co.jsol.jem.common.infrastructure.utils

fun Int?.isOverZero(): Boolean {
    return this != null && this > 0
}
