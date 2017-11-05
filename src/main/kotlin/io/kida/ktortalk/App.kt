package io.kida.ktortalk

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import io.kida.ktortalk.api.Motorcycles
import io.kida.ktortalk.extension.equalsCaseInsensitive
import io.kida.ktortalk.extension.jsonString
import io.kida.ktortalk.extension.respondJson
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
    // Set default `User-Agent: Chrome` as otherwise we'll be running into the CloudFlare blacklist
    FuelManager.instance.baseHeaders = mapOf("User-Agent" to "Chrome")

    // Instantiate our Ktor Application using an embedded Netty Server
    val server = embeddedServer(Netty, 8080) {
        install(BearerAuthentication("passw0rd"))
        routing {
            get("/") {
                val motorcycles = Motorcycles.getAll()
                        .groupBy { it.make }

                // Send response to client
                call.respondJson(motorcycles)
            }

            get("/{make}") {
                val motorcycles = Motorcycles.getAll()
                        .filter { it.make.equalsCaseInsensitive(call.parameters["make"]) }

                call.respondJson(motorcycles)
            }
        }
    }
    server.start(wait = true)
}
