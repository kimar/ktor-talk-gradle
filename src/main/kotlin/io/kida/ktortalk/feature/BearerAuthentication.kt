package io.kida.ktortalk.feature

import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.AttributeKey

class BearerAuthentication(val token: String): ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {
    override val key = AttributeKey<Unit>("Authentication")

    override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
        pipeline.intercept(ApplicationCallPipeline.Infrastructure) {
            when(call.request.queryParameters["token"]) {
                token -> proceed()
                else -> {
                    call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    finish()
                }
            }
        }
    }
}