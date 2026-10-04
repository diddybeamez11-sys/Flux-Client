package com.fluxclient.features

import com.fluxclient.core.PacketContext
import com.fluxclient.core.PacketDirection
import com.fluxclient.core.PacketDecision
import kotlin.test.Test
import kotlin.test.assertEquals

class FeatureRegistryTest {
    @Test fun disabledFeaturesDoNotObservePackets() {
        val stats = NetworkStatsFeature().also { it.enabled = false }
        val registry = FeatureRegistry().also { it.register(stats) }
        assertEquals(PacketDecision.FORWARD, registry.evaluate(PacketContext(PacketDirection.CLIENT_TO_SERVER, 1, byteArrayOf(), 1)))
        assertEquals(0, stats.packetsFromClient)
    }

    @Test fun statsAreResetAtSessionBoundary() {
        val stats = NetworkStatsFeature()
        val registry = FeatureRegistry().also { it.register(stats) }
        registry.evaluate(PacketContext(PacketDirection.SERVER_TO_CLIENT, 1, byteArrayOf(), 1))
        registry.closeSession()
        assertEquals(0, stats.packetsFromServer)
    }
}
