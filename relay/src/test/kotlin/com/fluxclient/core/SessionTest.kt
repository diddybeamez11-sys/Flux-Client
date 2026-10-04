package com.fluxclient.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class SessionTest {
    @Test fun tracksLifecycleAndCounters() {
        val session = FluxSession("test", "example.org", 19132)
        session.connected()
        session.recordReceived()
        session.recordSent()
        assertEquals(1, session.packetsReceived)
        assertEquals(1, session.packetsSent)
        session.close()
        assertEquals(FluxSession.State.CLOSED, session.currentState)
    }

    @Test fun countersCannotBeUsedBeforeConnect() {
        assertFails { FluxSession("test", "example.org", 19132).recordReceived() }
    }
}
