package io.kida.ktortalk.api

import com.github.kittinunf.fuel.httpGet
import io.kida.ktortalk.model.Motorcycle
import kotlin.coroutines.experimental.suspendCoroutine

class Motorcycles {
    companion object {
        suspend fun getAll(): List<Motorcycle> {
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
    }
}