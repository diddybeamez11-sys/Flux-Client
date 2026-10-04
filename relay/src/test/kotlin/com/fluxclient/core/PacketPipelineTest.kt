package com.fluxclient.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PacketPipelineTest {
    @Test fun forwardsAndCopiesPayload() {
        val pipeline = PacketPipeline(16)
        val original = byteArrayOf(1, 2)
        val result = pipeline.process(PacketDirection.CLIENT_TO_SERVER, 7, original)
        original[0] = 9
        assertEquals(PacketDecision.FORWARD, result.decision)
        assertEquals(1, result.packet!!.payload[0])
    }

    @Test fun interceptorCanDrop() {
        val pipeline = PacketPipeline(16)
        pipeline.add(PacketInterceptor { PacketDecision.DROP })
        assertEquals(PacketDecision.DROP, pipeline.process(PacketDirection.SERVER_TO_CLIENT, 1, byteArrayOf()).decision)
    }

    @Test fun rejectsOversizedPackets() {
        assertFailsWith<IllegalArgumentException> { PacketPipeline(1).process(PacketDirection.CLIENT_TO_SERVER, 1, byteArrayOf(1, 2)) }
    }
}
