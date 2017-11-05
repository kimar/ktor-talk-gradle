package io.kida.ktortalk.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson

data class Motorcycle(val id: Int, val make: String, val model: String, val displacement: Float, val year: Int, var image: String?) {
    class ListDeserializer: ResponseDeserializable<List<Motorcycle>> {
        override fun deserialize(content: String) = Gson().fromJson<List<Motorcycle>>(content)
    }
}