package com.fluxclient.model

/** User-editable connection settings kept separate from the running relay. */
data class ConnectionDraft(
    val host: String = "",
    val port: String = "19132"
) {
    fun validate(): String? {
        if (host.isBlank()) return "Enter a Bedrock server host"
        val parsed = port.toIntOrNull() ?: return "Port must be a number"
        if (parsed !in 1..65535) return "Port must be between 1 and 65535"
        return null
    }
}
