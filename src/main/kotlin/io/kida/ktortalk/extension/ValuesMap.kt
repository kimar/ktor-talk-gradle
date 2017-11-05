package io.kida.ktortalk.extension

import io.ktor.util.ValuesMap

fun ValuesMap.toListPair(): List<Pair<String, Any?>> {
    return this.names().map {
        Pair(it, this[it])
    }
}