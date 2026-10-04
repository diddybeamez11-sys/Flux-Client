package com.fluxclient.features

import com.fluxclient.core.PacketContext
import com.fluxclient.core.PacketDecision

/** World-change observer used by map/HUD features; never changes server state. */
class WorldFeature : FluxFeature {
    override val id = "world.observer"
    override val title = "World observer"
    override val category = FluxFeature.Category.WORLD
    override var enabled = false
    val showChunks = BooleanSetting("show_chunk_updates", true)
    val maxTrackedChunks = IntSetting("max_tracked_chunks", 256, 16..4096)
    private var updates = 0
    val chunkUpdates: Int get() = updates
    override fun onPacket(packet: PacketContext): PacketDecision {
        if (packet.payload.isNotEmpty()) updates = (updates + 1).coerceAtMost(maxTrackedChunks.value)
        return PacketDecision.FORWARD
    }
    override fun onSessionClosed() { updates = 0 }
}
