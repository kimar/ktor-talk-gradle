package io.kida.ktortalk.route

import com.github.kittinunf.fuel.httpGet
import io.kida.ktortalk.extension.respondJson
import io.kida.ktortalk.model.Motorcycle
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlin.coroutines.experimental.suspendCoroutine

suspend fun fetchMotorcycles(): List<Motorcycle> {
    return suspendCoroutine {
        "https://s.kida.io/mock-api/motorcycles.json".httpGet().responseObject(Motorcycle.ListDeserializer()) {
            _, _, result ->
            when(result.component1()) {
                null -> it.resumeWithException(Throwable("No Motorcycles found"))
                else -> it.resume(result.get())
            }
        }
    }
}
fun Routing.rest(): Route {
    return get("/") {
        val motorcycles = fetchMotorcycles()
                .filter { call.request.queryParameters["make"]?.contains(it.make, true) ?: true }
                .map { it.image = "https://s.kida.io/mock-api/motorcycles/${it.id}/image.jpg"; it }
                .groupBy { it.make }

        call.respondJson(motorcycles)
    }
}