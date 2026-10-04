package com.fluxclient.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class FluxRelayService : Service() {
    companion object {
        private const val CHANNEL = "flux_relay"
        private const val ID = 42
        const val ACTION_STOP = "com.fluxclient.action.STOP"
    }

    override fun onCreate() {
        super.onCreate()
        val notifications = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifications.createNotificationChannel(
                NotificationChannel(CHANNEL, "Flux relay", NotificationManager.IMPORTANCE_LOW)
            )
        }
        startForeground(ID, notification("Flux relay is ready"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            RelayController.stop()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() { RelayController.stop(); super.onDestroy() }
    override fun onBind(intent: Intent?): IBinder? = null

    private fun notification(message: String): Notification = NotificationCompat.Builder(this, CHANNEL)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Flux Client")
        .setContentText(message)
        .setOngoing(true)
        .build()
}
