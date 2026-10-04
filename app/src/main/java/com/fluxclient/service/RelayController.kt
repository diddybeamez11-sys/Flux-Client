package com.fluxclient.service

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fluxclient.core.FluxRelayEngine
import com.fluxclient.core.RelayConfig

/** Single owner of relay lifecycle; activities never own sockets or threads. */
object RelayController {
    private var appContext: Context? = null
    private var engine: FluxRelayEngine? = null

    var running by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun initialize(context: Context) { appContext = context.applicationContext }

    @Synchronized
    fun start(host: String, port: Int): Boolean {
        if (running) return true
        return runCatching {
            val created = FluxRelayEngine(
                RelayConfig(remoteHost = host, remotePort = port)
            )
            created.start()
            engine = created
            error = null
            running = true
            true
        }.getOrElse {
            error = it.message ?: "Unable to start Flux relay"
            false
        }
    }

    @Synchronized
    fun stop() {
        engine?.close()
        engine = null
        running = false
    }

    fun shutdown() = stop()
}
