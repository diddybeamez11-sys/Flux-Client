package com.fluxclient.core

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

/** Direction as seen by the connected Minecraft client. */
enum class PacketDirection { CLIENT_TO_SERVER, SERVER_TO_CLIENT }

data class PacketContext(
    val direction: PacketDirection,
    val packetId: Int,
    val payload: ByteArray,
    val sequence: Long
)

enum class PacketDecision { FORWARD, DROP }

fun interface PacketInterceptor {
    fun intercept(context: PacketContext): PacketDecision
}

data class PipelineResult(val decision: PacketDecision, val packet: PacketContext?)

/**
 * Small, deterministic packet middleware layer. It owns no socket and can be
 * tested independently from RakNet and Android. Interceptors run in order;
 * an interceptor may drop a packet without mutating the original byte array.
 */
class PacketPipeline(private val maxPacketBytes: Int) {
    private val interceptors = CopyOnWriteArrayList<PacketInterceptor>()
    private val sequence = AtomicLong()

    fun add(interceptor: PacketInterceptor) { interceptors += interceptor }
    fun remove(interceptor: PacketInterceptor) { interceptors -= interceptor }
    fun clear() = interceptors.clear()

    fun process(direction: PacketDirection, packetId: Int, payload: ByteArray): PipelineResult {
        require(payload.size <= maxPacketBytes) { "Packet exceeds configured size limit" }
        val context = PacketContext(direction, packetId, payload.copyOf(), sequence.incrementAndGet())
        for (interceptor in interceptors) {
            if (interceptor.intercept(context) == PacketDecision.DROP) {
                return PipelineResult(PacketDecision.DROP, null)
            }
        }
        return PipelineResult(PacketDecision.FORWARD, context)
    }
}
