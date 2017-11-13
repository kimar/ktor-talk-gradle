package io.kida.ktortalk.route

import com.github.kittinunf.fuel.httpGet
import io.kida.ktortalk.extension.respondJson
import io.kida.ktortalk.model.Make
import io.kida.ktortalk.model.Model
import io.kida.ktortalk.model.Motorcycle
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlin.coroutines.experimental.suspendCoroutine

suspend fun fetchMakes(): List<Make> {
    return suspendCoroutine {
        "https://s.kida.io/mock-api/makes.json".httpGet().responseObject(Make.ListDeserializer()) {
            _, _, result ->
            when(result.component1()) {
                null -> it.resumeWithException(Throwable("No Makes found"))
                else -> it.resume(result.get())
            }
        }
    }
}

suspend fun fetchModels(): List<Model> {
    return suspendCoroutine {
        "https://s.kida.io/mock-api/models.json".httpGet().responseObject(Model.ListDeserializer()) {
            _, _, result ->
            when(result.component1()) {
                null -> it.resumeWithException(Throwable("No Models found"))
                else -> it.resume(result.get())
            }
        }
    }
}

suspend fun fetchMergedMotorcycles(): List<Motorcycle> {
    val makes = fetchMakes()
    val models = fetchModels()

    return models.map { model ->
        Motorcycle.fromModel(model, makes.first { it.code == model.make})
    }
}

fun Routing.rest(): Route {
    return get("/") {
        val motorcycles = fetchMergedMotorcycles()
                .filter { call.request.queryParameters["make"]?.contains(it.make.name, true) ?: true }
                .map { it.image = "https://s.kida.io/mock-api/motorcycles/${it.id}/image.jpg"; it }
                .groupBy { it.make }

        call.respondJson(motorcycles)
    }
}