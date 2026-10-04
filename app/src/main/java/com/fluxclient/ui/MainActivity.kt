package com.fluxclient.ui

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
import com.fluxclient.service.RelayController
import com.fluxclient.ui.theme.FluxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FluxTheme { Surface { FluxHome() } } }
    }
}

@Composable
private fun FluxHome() {
    var host by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("19132") }
    var validation by remember { mutableStateOf<String?>(null) }
    val running by remember { derivedStateOf { RelayController.running } }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Flux Client", style = MaterialTheme.typography.headlineLarge)
        Text("A transparent Bedrock relay for networks you are authorized to test.")
        OutlinedTextField(host, { host = it; validation = null }, Modifier.fillMaxWidth(), label = { Text("Server host") })
        OutlinedTextField(port, { port = it; validation = null }, Modifier.fillMaxWidth(), label = { Text("Server port") })
        validation?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        RelayController.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(enabled = !running, onClick = {
                val draft = ConnectionDraft(host, port)
                validation = draft.validate()
                if (validation == null) RelayController.start(host.trim(), port.toInt())
            }) { Text("Start relay") }
            Button(enabled = running, onClick = { RelayController.stop() }) { Text("Stop") }
        }
        Text(if (running) "Status: running" else "Status: stopped")
    }
}
