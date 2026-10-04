package com.fluxclient.core

import java.time.Instant
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

/** A transport-neutral session owned by Flux rather than by the Android UI. */
class FluxSession(val id: String, val remoteHost: String, val remotePort: Int) : AutoCloseable {
    enum class State { CONNECTING, CONNECTED, CLOSING, CLOSED, FAILED }

    private val state = AtomicReference(State.CONNECTING)
    private val received = AtomicLong()
    private val sent = AtomicLong()
    val createdAt: Instant = Instant.now()
    val currentState: State get() = state.get()
    val packetsReceived: Long get() = received.get()
    val packetsSent: Long get() = sent.get()

    fun connected() { state.compareAndSet(State.CONNECTING, State.CONNECTED) }
    fun recordReceived() { check(currentState == State.CONNECTED); received.incrementAndGet() }
    fun recordSent() { check(currentState == State.CONNECTED); sent.incrementAndGet() }
    fun fail() { state.set(State.FAILED) }
    override fun close() { state.set(State.CLOSING); state.set(State.CLOSED) }
}
