package com.fluxclient.features

import com.fluxclient.core.PacketContext
import com.fluxclient.core.PacketDecision

/** Original Flux feature contract. Features observe or reject packets without owning transport. */
interface FluxFeature {
    val id: String
    val title: String
    val category: Category
    var enabled: Boolean

    enum class Category { VISUAL, NETWORK, WORLD, ACCESSIBILITY }
    fun onPacket(packet: PacketContext): PacketDecision = PacketDecision.FORWARD
    fun onSessionClosed() = Unit
}
