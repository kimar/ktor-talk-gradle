package io.kida.ktortalk.extension

import com.google.gson.Gson

fun Any.jsonString(): String {
    return Gson().toJson(this)
}