package io.kida.ktortalk.route

import com.github.pgutkowski.kgraphql.KGraphQL
import io.kida.ktortalk.api.Motorcycles
import io.kida.ktortalk.extension.equalsCaseInsensitive
import io.kida.ktortalk.model.Motorcycle
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.document
import io.ktor.request.httpMethod
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.*

fun Routing.graphQl(): Route {
    return route("/graphql") {
        get { handleGraphQl(call) }
        post { handleGraphQl(call) }
    }
}

suspend fun handleGraphQl(call: ApplicationCall) {
    val motorcycles = Motorcycles.getAll()
    val schema = KGraphQL.schema {

        configure {
            useDefaultPrettyPrinter = true
        }

        query("motorcycles") {
            resolver { make: String ->  {
                motorcycles.filter {
                    it.make.equalsCaseInsensitive(make)
                }
            } }
        }

        type<Motorcycle>()
    }

    val result = when(call.request.httpMethod) {
        HttpMethod.Get -> schema.execute(call.request.queryParameters["query"]!!)
        HttpMethod.Post -> schema.execute(call.receiveText())
        else -> "Method not supported"
    }

    call.respond(result)
}