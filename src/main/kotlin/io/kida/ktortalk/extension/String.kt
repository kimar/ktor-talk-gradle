package io.kida.ktortalk.extension

fun String.equalsCaseInsensitive(other: String?): Boolean {
    return when(other) {
        null -> true
        else -> toLowerCase() == other.toLowerCase()
    }
}