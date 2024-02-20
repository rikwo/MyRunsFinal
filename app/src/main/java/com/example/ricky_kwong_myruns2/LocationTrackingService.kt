package com.example.ricky_kwong_myruns2

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class LocationTrackingService : Service(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): LocationTrackingService = this@LocationTrackingService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startID: Int): Int {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        initLocationManager()

        return START_STICKY
    }

    private fun stopForgroundService() {
        stopForeground(true)
        stopSelf()
    }

    private fun initLocationManager() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)

    }

    override fun onLocationChanged(location: Location) {

    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MapActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("MyRuns is recording your path").setContentText("Tap to open MyRuns").setSmallIcon(android.R.drawable.ic_notification_overlay).setContentIntent(pendingIntent).build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "MyRuns Channel", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "MyRunsChannel"
    }
}