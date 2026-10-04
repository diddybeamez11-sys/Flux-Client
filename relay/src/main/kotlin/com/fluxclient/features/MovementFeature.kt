package com.fluxclient.features

import com.fluxclient.core.PacketContext
import com.fluxclient.core.PacketDecision
import java.util.concurrent.atomic.AtomicLong

/** Movement diagnostics with a conservative packet budget for mobile devices. */
class MovementFeature : FluxFeature {
    override val id = "movement.diagnostics"
    override val title = "Movement diagnostics"
    override val category = FluxFeature.Category.ACCESSIBILITY
    override var enabled = false
    val showCoordinates = BooleanSetting("show_coordinates", true)
    val updateRate = IntSetting("updates_per_second", 10, 1..30)
    private val samples = AtomicLong()
    val sampleCount: Long get() = samples.get()
    override fun onPacket(packet: PacketContext): PacketDecision {
        if (packet.payload.isNotEmpty()) samples.incrementAndGet()
        return PacketDecision.FORWARD
    }
    override fun onSessionClosed() { samples.set(0) }
}
