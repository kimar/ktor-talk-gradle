package io.kida.ktortalk

import com.github.kittinunf.fuel.core.FuelManager
import io.kida.ktortalk.feature.BearerAuthentication
import io.kida.ktortalk.route.graphQl
import io.kida.ktortalk.route.rest
import io.ktor.application.install
import io.ktor.routing.routing
import io.ktor.server.host.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    // Set default `User-Agent: Chrome` as otherwise we'll be running into the CloudFlare blacklist
    FuelManager.instance.baseHeaders = mapOf("User-Agent" to "Chrome")

    // Instantiate our Ktor Application using an embedded Netty Server
    val server = embeddedServer(Netty, 8080) {
        install(BearerAuthentication("passw0rd"))
        routing {
            graphQl()
            rest()
        }
    }
    server.start(wait = true)
}
