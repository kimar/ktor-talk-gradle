package io.kida.ktortalk.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson

data class Make(val code: Int, val name: String) {
    class ListDeserializer: ResponseDeserializable<List<Make>> {
        override fun deserialize(content: String) = Gson().fromJson<List<Make>>(content)
    }
}