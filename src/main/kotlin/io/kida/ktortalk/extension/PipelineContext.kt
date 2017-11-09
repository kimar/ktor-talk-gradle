package io.kida.ktortalk.extension

import io.ktor.pipeline.PipelineContext

suspend fun <TSubject: Any, TContext: Any> PipelineContext<TSubject, TContext>.finishWith(closure: suspend () -> Unit) {
    closure()
    finish()
}