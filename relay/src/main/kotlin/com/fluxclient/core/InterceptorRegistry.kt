package com.fluxclient.core

import java.util.concurrent.CopyOnWriteArrayList

/** Thread-safe registry used by Flux features without coupling them to transport. */
class InterceptorRegistry {
    private val entries = CopyOnWriteArrayList<Pair<String, PacketInterceptor>>()

    fun register(name: String, interceptor: PacketInterceptor) {
        require(name.isNotBlank()) { "Interceptor name must not be blank" }
        entries.removeIf { it.first == name }
        entries += name to interceptor
    }

    fun unregister(name: String) { entries.removeIf { it.first == name } }
    fun names(): List<String> = entries.map { it.first }
    fun installInto(pipeline: PacketPipeline) { entries.forEach { pipeline.add(it.second) } }
}
