package com.fluxclient.features

import com.fluxclient.core.PacketContext
import com.fluxclient.core.PacketDecision
import java.util.concurrent.atomic.AtomicLong

/**
 * Combat information module. It deliberately observes protocol traffic only;
 * it never fabricates attacks or targets, making it safe and deterministic.
 */
class CombatFeature : FluxFeature {
    override val id = "combat.telemetry"
    override val title = "Combat telemetry"
    override val category = FluxFeature.Category.VISUAL
    override var enabled = false
    val showTargetInfo = BooleanSetting("show_target_info", true)
    val range = DoubleSetting("display_range", 32.0, 1.0..128.0)
    private val observed = AtomicLong()
    val observations: Long get() = observed.get()
    override fun onPacket(packet: PacketContext): PacketDecision {
        if (packet.payload.isNotEmpty()) observed.incrementAndGet()
        return PacketDecision.FORWARD
    }
    override fun onSessionClosed() { observed.set(0) }
}
