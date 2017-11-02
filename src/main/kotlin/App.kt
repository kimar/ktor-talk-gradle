import com.github.kittinunf.fuel.httpGet
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.AutoHeadResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.host.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.AttributeKey
import io.ktor.util.ValuesMap
import kotlin.coroutines.experimental.suspendCoroutine

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, 8080) {
        routing {
            install(BearerAuthentication("passw0rd"))
            get("/") {
                call.respondText(getStuff(call.parameters.toListPair()))
            }
        }
    }
    server.start(wait = true)
}

class BearerAuthentication(val token: String): ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {
    override val key = AttributeKey<Unit>("Authentication")

    override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
        pipeline.intercept(ApplicationCallPipeline.Infrastructure) {
            when(call.request.queryParameters["token"]) {
                token -> proceed()
                else -> call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
            }
        }
    }
}

fun ValuesMap.toListPair(): List<Pair<String, Any?>> {
    return this.names().map {
        Pair(it, this[it])
    }
}

suspend fun getStuff(params: List<Pair<String, Any?>>?): String {
    return suspendCoroutine {
        "https://httpbin.org/get".httpGet(
                params
        ).responseString { req, res, result ->
            when(result.component1()) {
                null -> it.resumeWithException(Throwable("Invalid response"))
                else -> it.resume(result.get())
            }
        }
    }
}
