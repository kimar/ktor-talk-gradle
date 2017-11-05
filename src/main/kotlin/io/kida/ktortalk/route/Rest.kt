package io.kida.ktortalk.route

import io.kida.ktortalk.api.Motorcycles
import io.kida.ktortalk.extension.equalsCaseInsensitive
import io.ktor.http.HttpMethod
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.method
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.routing.*
import io.kida.ktortalk.extension.respondJson

fun Routing.rest(): Route {
    return get("/") {
        val motorcycles = Motorcycles.getAll()
                .filter { it.make.equalsCaseInsensitive(call.request.queryParameters["make"]) }
                .groupBy { it.make }

        call.respondJson(motorcycles)
    }
}