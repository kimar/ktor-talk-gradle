package io.kida.ktortalk.extension

import io.ktor.application.ApplicationCall
import io.ktor.response.respondText

suspend fun ApplicationCall.respondJson(obj: Any) {
    respondText(obj.jsonString())
}