package kr.co.jsol.jem.common.infrastructure.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/*

 */
@OptIn(ExperimentalContracts::class)
inline fun <T> T?.exists(block: (T) -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (this != null) {
        // 만약 this 가 String 타입이라면 Blank 체크를 추가로 진행
        if (this is String) {
            if (this.isBlank()) {
                return
            }
        }

        if (this is List<*>) {
            if (this.isEmpty()) {
                return
            }
        }

        // 값이 존재하여 정상적으로 처리
        block(this)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T : Number> T?.nonNegative(block: (T) -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (this != null) {
        // 0 미만(음수)일때 로직 처리 X
        if (this.toInt() < 0) return

        // 값이 존재하여 정상적으로 처리
        block(this)
    }
}

