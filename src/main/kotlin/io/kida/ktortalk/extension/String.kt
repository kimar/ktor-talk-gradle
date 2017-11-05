package io.kida.ktortalk.extension

fun String.equalsCaseInsensitive(other: String?): Boolean {
    return toLowerCase() == other?.toLowerCase()
}