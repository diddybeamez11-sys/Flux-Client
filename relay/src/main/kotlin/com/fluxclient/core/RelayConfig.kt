package com.fluxclient.core

/** Immutable configuration for a Flux relay instance. */
data class RelayConfig(
    val listenHost: String = "0.0.0.0",
    val listenPort: Int = 19132,
    val remoteHost: String,
    val remotePort: Int = 19132,
    val maxQueuedPackets: Int = 512,
    val maxPacketBytes: Int = 2 * 1024 * 1024
) {
    init {
        require(listenPort in 1..65535) { "listenPort must be a valid TCP/UDP port" }
        require(remotePort in 1..65535) { "remotePort must be a valid TCP/UDP port" }
        require(remoteHost.isNotBlank()) { "remoteHost must not be blank" }
        require(maxQueuedPackets > 0) { "maxQueuedPackets must be positive" }
        require(maxPacketBytes > 0) { "maxPacketBytes must be positive" }
    }
}
