package com.fluxclient.features

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Server-authorized flight controller for private worlds/test servers.
 * It does not alter timers, spoof packets, or attempt to bypass anti-cheat.
 */
class FlightController(
    horizontalSpeed: Double = 1.5,
    verticalSpeed: Double = 0.5
) {
    var horizontalSpeed: Double = horizontalSpeed.coerceIn(0.0, 4.0)
        set(value) { field = value.coerceIn(0.0, 4.0) }
    var verticalSpeed: Double = verticalSpeed.coerceIn(0.0, 2.0)
        set(value) { field = value.coerceIn(0.0, 2.0) }

    fun velocity(input: FlightInput, yawDegrees: Double, currentY: Double): FlightVelocity {
        if (!input.authorized || input.sneaking || input.inFluid) return FlightVelocity(0.0, currentY, 0.0)
        val x = input.strafe.coerceIn(-1.0, 1.0)
        val z = input.forward.coerceIn(-1.0, 1.0)
        val length = sqrt(x * x + z * z)
        if (length == 0.0) return FlightVelocity(0.0, currentY, 0.0)
        val nx = x / length
        val nz = z / length
        val yaw = Math.toRadians(yawDegrees)
        val forwardX = -sin(yaw)
        val forwardZ = cos(yaw)
        val rightX = cos(yaw)
        val rightZ = sin(yaw)
        return FlightVelocity(
            (forwardX * nz + rightX * nx) * horizontalSpeed,
            input.vertical.coerceIn(-1.0, 1.0) * verticalSpeed,
            (forwardZ * nz + rightZ * nx) * horizontalSpeed
        )
    }
}

data class FlightInput(
    val forward: Double,
    val strafe: Double,
    val vertical: Double,
    val authorized: Boolean,
    val sneaking: Boolean = false,
    val inFluid: Boolean = false
)

data class FlightVelocity(val x: Double, val y: Double, val z: Double)
