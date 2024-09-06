package kr.co.jsol.jem.common.infrastructure.utils

fun parseQueryParamsToString(queryParams: Map<String, Any>): String {
    return "?${queryParams.map { "${it.key}=${it.value}" }.joinToString("&")}"
}
