package io.kida.ktortalk.model

data class Motorcycle(val id: Int, val make: Make, val model: String, val displacement: Float, val year: Int, var image: String?) {
    companion object {
        fun fromModel(model: Model, make: Make): Motorcycle {
            return Motorcycle(model.id, make, model.model, model.displacement, model.year, model.image)
        }
    }
}