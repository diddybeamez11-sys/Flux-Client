package com.fluxclient.features

import com.fluxclient.core.PacketContext
import com.fluxclient.core.PacketDirection
import com.fluxclient.core.PacketDecision
import java.util.concurrent.atomic.AtomicLong

/** Safer replacement for opaque packet displays: exposes only aggregate counters. */
class NetworkStatsFeature : FluxFeature {
    override val id = "network.stats"
    override val title = "Network statistics"
    override val category = FluxFeature.Category.NETWORK
    override var enabled = true

    private val clientToServer = AtomicLong()
    private val serverToClient = AtomicLong()
    val packetsFromClient: Long get() = clientToServer.get()
    val packetsFromServer: Long get() = serverToClient.get()

    override fun onPacket(packet: PacketContext): PacketDecision {
        when (packet.direction) {
            PacketDirection.CLIENT_TO_SERVER -> clientToServer.incrementAndGet()
            PacketDirection.SERVER_TO_CLIENT -> serverToClient.incrementAndGet()
        }
        return PacketDecision.FORWARD
    }

    override fun onSessionClosed() {
        clientToServer.set(0)
        serverToClient.set(0)
    }
}
