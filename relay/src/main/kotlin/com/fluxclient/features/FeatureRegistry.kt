package com.fluxclient.features

import com.fluxclient.core.PacketContext
import com.fluxclient.core.PacketDecision
import java.util.concurrent.CopyOnWriteArrayList

/** Enables features by stable id and evaluates them in deterministic order. */
class FeatureRegistry {
    private val features = CopyOnWriteArrayList<FluxFeature>()

    fun register(feature: FluxFeature) {
        require(features.none { it.id == feature.id }) { "Feature already registered: ${feature.id}" }
        features += feature
    }

    fun find(id: String): FluxFeature? = features.firstOrNull { it.id == id }
    fun all(): List<FluxFeature> = features.toList()
    fun enabled(): List<FluxFeature> = features.filter { it.enabled }

    fun evaluate(packet: PacketContext): PacketDecision {
        for (feature in features) {
            if (feature.enabled && feature.onPacket(packet) == PacketDecision.DROP) return PacketDecision.DROP
        }
        return PacketDecision.FORWARD
    }

    fun closeSession() = features.forEach { it.onSessionClosed() }
}
