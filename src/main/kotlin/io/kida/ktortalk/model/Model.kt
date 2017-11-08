package io.kida.ktortalk.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson

data class Model(val id: Int, val make: Int, val model: String, val displacement: Float, val year: Int, var image: String?) {
    class ListDeserializer: ResponseDeserializable<List<Model>> {
        override fun deserialize(content: String) = Gson().fromJson<List<Model>>(content)
    }
}