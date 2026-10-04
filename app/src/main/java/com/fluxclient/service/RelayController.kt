package com.fluxclient.service

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fluxclient.relay.FluxRelay
import com.fluxclient.relay.address.FluxAddress
import com.fluxclient.relay.listener.AutoCodecPacketListener
import com.fluxclient.relay.listener.GamingPacketHandler
import com.fluxclient.relay.listener.OnlineLoginPacketListener
import com.fluxclient.relay.util.captureGamePacket
import net.raphimc.minecraftauth.step.bedrock.session.StepFullBedrockSession
import kotlin.concurrent.thread

/** Owns the real RakNet/Bedrock relay. UI only submits validated connection settings. */
object RelayController {
    private var appContext: Context? = null
    private var relay: FluxRelay? = null
    private var worker: Thread? = null

    var running by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun initialize(context: Context) { appContext = context.applicationContext }

    @Synchronized
    fun start(host: String, port: Int, account: StepFullBedrockSession.FullBedrockSession? = null): Boolean {
        if (running || worker != null) return true
        error = null
        worker = thread(name = "FluxRelay", start = true) {
            runCatching {
                val remote = FluxAddress(host.trim(), port)
                relay = captureGamePacket(
                    localAddress = FluxAddress("0.0.0.0", 19132),
                    remoteAddress = remote
                ) {
                    listeners.add(AutoCodecPacketListener(this))
                    account?.let { listeners.add(OnlineLoginPacketListener(this, it)) }
                    listeners.add(GamingPacketHandler(this))
                }
                running = true
            }.onFailure {
                error = it.message ?: "Unable to start Flux relay"
                running = false
            }
        }
        return true
    }

    @Synchronized
    fun stop() {
        relay?.stop()
        relay = null
        running = false
        worker?.interrupt()
        worker = null
    }

    fun shutdown() = stop()
}
