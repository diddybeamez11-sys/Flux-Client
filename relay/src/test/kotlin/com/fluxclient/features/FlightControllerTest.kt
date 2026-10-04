package com.fluxclient.features

import kotlin.test.Test
import kotlin.test.assertEquals

class FlightControllerTest {
    @Test fun unauthorizedFlightProducesNoMovement() {
        val result = FlightController().velocity(FlightInput(1.0, 0.0, 1.0, authorized = false), 0.0, 0.25)
        assertEquals(FlightVelocity(0.0, 0.25, 0.0), result)
    }

    @Test fun diagonalInputDoesNotIncreaseSpeed() {
        val result = FlightController(2.0, 1.0).velocity(FlightInput(1.0, 1.0, 0.0, true), 0.0, 0.0)
        assertEquals(2.0, kotlin.math.sqrt(result.x * result.x + result.z * result.z), 0.0001)
    }
}
