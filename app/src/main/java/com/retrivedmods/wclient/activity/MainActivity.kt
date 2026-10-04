package com.retrivedmods.wclient.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.retrivedmods.wclient.game.ModuleManager
import com.retrivedmods.wclient.navigation.Navigation
import com.retrivedmods.wclient.ui.component.LoadingScreen
import com.retrivedmods.wclient.ui.theme.WClientTheme
import com.retrivedmods.wclient.util.SoundUtil

class MainActivity : ComponentActivity() {

    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoundUtil.load(applicationContext)

        ModuleManager.loadConfig()

        enableEdgeToEdge()
        setupImmersiveMode()
        checkBatteryOptimizations()


        setContent {
            WClientTheme {
                var showLoading by remember { mutableStateOf(true) }

                if (showLoading) {
                    LoadingScreen(
                        onDone = {
                            showLoading = false
                        }
                    )
                } else {
                    Navigation()
                }
            }
        }
    }

    private fun setupImmersiveMode() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    @SuppressLint("BatteryLife")
    private fun checkBatteryOptimizations() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            startActivity(
                Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = "package:$packageName".toUri()
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ModuleManager.saveConfig()
    }
}