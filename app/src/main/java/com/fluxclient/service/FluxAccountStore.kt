package com.fluxclient.service

import android.content.Context
import com.fluxclient.relay.util.authorize
import net.raphimc.minecraftauth.step.bedrock.session.StepFullBedrockSession
import net.raphimc.minecraftauth.step.msa.StepMsaDeviceCode
import java.io.File

/** Keeps the device-code session inside app-private storage. */
object FluxAccountStore {
    private const val FILE = "flux-bedrock-session.json"
    fun load(context: Context): StepFullBedrockSession.FullBedrockSession? {
        val file = File(context.filesDir, FILE)
        if (!file.exists()) return null
        return runCatching { authorize(cache = true, file = file, msaDeviceCodeCallback = StepMsaDeviceCode.MsaDeviceCodeCallback {}) }.getOrNull()
    }

    fun login(context: Context, callback: net.raphimc.minecraftauth.step.msa.StepMsaDeviceCode.MsaDeviceCodeCallback): StepFullBedrockSession.FullBedrockSession =
        authorize(cache = true, file = File(context.filesDir, FILE), msaDeviceCodeCallback = callback)
}
