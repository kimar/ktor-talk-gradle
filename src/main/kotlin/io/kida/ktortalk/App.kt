package io.kida.ktortalk

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import io.kida.ktortalk.api.Motorcycles
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
            get("/") {
                call.respondText(Motorcycles.getAll().toString())
            }
        }
    }
    server.start(wait = true)
}
