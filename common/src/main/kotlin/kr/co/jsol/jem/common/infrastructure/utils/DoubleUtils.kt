package kr.co.jsol.jem.common.infrastructure.utils

class DoubleUtils

// 소수점이 .0 으로 끝이날 경우  소수점 을 없애는 처리
fun Double.toStringWithOutPrimeNumber(): String {
    if ((this.toString().substringAfter(".")).toInt() > 0) {
        return this.toString()
    } else {
        return this.toString().substringBefore(".")
    }
}

