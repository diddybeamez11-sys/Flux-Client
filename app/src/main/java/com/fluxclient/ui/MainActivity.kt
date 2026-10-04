package com.fluxclient.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fluxclient.model.ConnectionDraft
import com.fluxclient.service.FluxAccountStore
import com.fluxclient.service.RelayController
import net.raphimc.minecraftauth.step.msa.StepMsaDeviceCode
import com.fluxclient.ui.theme.FluxTheme
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FluxTheme { Surface { FluxHome() } } }
    }

    @Composable
    private fun FluxHome() {
        var host by remember { mutableStateOf("") }
        var port by remember { mutableStateOf("19132") }
        var validation by remember { mutableStateOf<String?>(null) }
        var loginMessage by remember { mutableStateOf("No Microsoft account selected") }
        var account by remember { mutableStateOf(FluxAccountStore.load(this@MainActivity)) }
        val running by remember { derivedStateOf { RelayController.running } }

        Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Flux Client", style = MaterialTheme.typography.headlineLarge)
            Text("A transparent Bedrock relay for networks you are authorized to test.")
            OutlinedTextField(host, { host = it; validation = null }, Modifier.fillMaxWidth(), label = { Text("Server host") })
            OutlinedTextField(port, { port = it; validation = null }, Modifier.fillMaxWidth(), label = { Text("Server port") })
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    loginMessage = "Opening Microsoft device login..."
                    thread(name = "FluxLogin") {
                        runCatching {
                            FluxAccountStore.login(this@MainActivity, StepMsaDeviceCode.MsaDeviceCodeCallback { device ->
                                runOnUiThread {
                                    loginMessage = "Enter the displayed Microsoft code in your browser"
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(device.directVerificationUri)))
                                }
                            })
                        }.onSuccess { session ->
                            runOnUiThread {
                                account = session
                                loginMessage = "Signed in as ${session.mcChain.displayName}"
                            }
                        }.onFailure { error ->
                            runOnUiThread { loginMessage = "Login failed: ${error.message}" }
                        }
                    }
                }) { Text("Microsoft login") }
                Text(loginMessage, modifier = Modifier.padding(top = 12.dp))
            }
            validation?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            RelayController.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(enabled = !running, onClick = {
                    val draft = ConnectionDraft(host, port)
                    validation = draft.validate()
                    if (validation == null && RelayController.start(host.trim(), port.toInt(), account)) {
                        packageManager.getLaunchIntentForPackage("com.mojang.minecraftpe")?.let(::startActivity)
                            ?: run { validation = "Minecraft Bedrock is not installed" }
                    }
                }) { Text("Start relay and Minecraft") }
                Button(enabled = running, onClick = { RelayController.stop() }) { Text("Stop") }
            }
            Text(if (running) "Status: relay running; connect Minecraft to this device on port 19132" else "Status: stopped")
        }
    }
}
