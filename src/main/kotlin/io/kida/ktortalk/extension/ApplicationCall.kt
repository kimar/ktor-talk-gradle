package io.kida.ktortalk.extension

import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText

suspend fun ApplicationCall.respondJson(obj: Any, contentType: ContentType? = null, statusCode: HttpStatusCode? = HttpStatusCode.OK) {
    respondText(obj.jsonString(), contentType, statusCode)
}