package com.fluxclient.core

import java.util.concurrent.atomic.AtomicReference

/** Lifecycle boundary for Flux. Transport adapters call [accept] for decoded packets. */
class FluxRelayEngine(val config: RelayConfig) : AutoCloseable {
    enum class State { STOPPED, STARTING, RUNNING, STOPPING, FAILED }

    private val state = AtomicReference(State.STOPPED)
    val pipeline = PacketPipeline(config.maxPacketBytes)
    val currentState: State get() = state.get()

    @Synchronized
    fun start() {
        check(state.compareAndSet(State.STOPPED, State.STARTING)) { "Relay is already active" }
        try {
            // The transport adapter is intentionally injected in the next layer;
            // this core never opens an unexpected socket by itself.
            state.set(State.RUNNING)
        } catch (error: Throwable) {
            state.set(State.FAILED)
            throw error
        }
    }

    fun accept(direction: PacketDirection, packetId: Int, payload: ByteArray): PipelineResult {
        check(state.get() == State.RUNNING) { "Relay is not running" }
        return pipeline.process(direction, packetId, payload)
    }

    @Synchronized
    override fun close() {
        if (state.get() == State.STOPPED) return
        state.set(State.STOPPING)
        state.set(State.STOPPED)
    }
}
