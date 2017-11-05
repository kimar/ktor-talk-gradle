package io.kida.ktortalk

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import io.kida.ktortalk.feature.BearerAuthentication
import io.kida.ktortalk.model.Motorcycle
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.host.embeddedServer
import io.ktor.server.netty.Netty
import kotlin.coroutines.experimental.suspendCoroutine

fun main(args: Array<String>) {
    FuelManager.instance.baseHeaders = mapOf("User-Agent" to "Chrome")

    val server = embeddedServer(Netty, 8080) {
        install(BearerAuthentication("passw0rd"))
        routing {
            get(    "/") {
                call.respondText(getMotorcycles().toString())
            }
        }
    }
    server.start(wait = true)
}

suspend fun getMotorcycles(): List<Motorcycle> {
    return suspendCoroutine {
        "https://s.kida.io/mock-api/motorcycles.json".httpGet().responseObject(Motorcycle.ListDeserializer()) {
            _, res, result ->

            print(res)
            print(result)
            when(result.component1()) {
                null -> it.resumeWithException(Throwable("No Motorcycles found"))
                else -> it.resume(result.get())
            }
        }
    }
}