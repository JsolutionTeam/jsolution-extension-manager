package kr.co.jsol.jem.common.infrastructure.utils

class LongUtils

fun Long?.isOverZero(): Boolean {
    return this != null && this > 0
}
