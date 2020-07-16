package com.qwert2603.geotrack

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TheForegroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManagerCompat = NotificationManagerCompat.from(this)
            notificationManagerCompat.createNotificationChannel(
                NotificationChannel(
                    "ch",
                    "ch",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        startForeground(
            1, NotificationCompat.Builder(this, "ch")
                .setTicker("ticker")
                .setContentTitle("title")
                .setOngoing(true)
                .setAutoCancel(false)
                .build()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}