package org.traccar.client

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import org.traccar.sdk.Constants
import org.traccar.sdk.TrackingController
import org.traccar.sdk.WakefulBroadcastReceiver


class TrackingService : Service() {

    private var wakeLock: WakeLock? = null
    private var trackingController: TrackingController? = null

    class HideNotificationService : Service() {
        override fun onBind(intent: Intent): IBinder? {
            return null
        }

        override fun onCreate() {
            startForeground(NOTIFICATION_ID, createNotification(this))
            stopForeground(true)
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            stopSelfResult(startId)
            return START_NOT_STICKY
        }
    }

    @SuppressLint("WakelockTimeout")
    override fun onCreate() {
        startForeground(NOTIFICATION_ID, createNotification(this))
        Log.i(TAG, "service create")

        sendBroadcast(Intent(ACTION_STARTED))
        StatusActivity.addMessage(getString(R.string.status_service_create))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.KEY_WAKELOCK, true)) {
                val powerManager = getSystemService(POWER_SERVICE) as PowerManager
                wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, javaClass.name)
                wakeLock?.acquire()
            }
            trackingController = TrackingController(this)
            trackingController?.start()
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, Intent(this, HideNotificationService::class.java))
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        WakefulBroadcastReceiver.completeWakefulIntent(intent)
        return START_STICKY
    }

    override fun onDestroy() {
        stopForeground(true)
        Log.i(TAG, "service destroy")
        sendBroadcast(Intent(ACTION_STOPPED))
        StatusActivity.addMessage(getString(R.string.status_service_destroy))
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
        trackingController?.stop()
    }

    companion object {

        const val ACTION_STARTED = "org.traccar.action.SERVICE_STARTED"
        const val ACTION_STOPPED = "org.traccar.action.SERVICE_STOPPED"
        private val TAG = TrackingService::class.java.simpleName
        private const val NOTIFICATION_ID = 1

        @SuppressLint("UnspecifiedImmutableFlag")
        private fun createNotification(context: Context): Notification {
            //TODO: Icon
            val builder = NotificationCompat.Builder(context, Constants.PRIMARY_CHANNEL)
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
            //TODO: Verify
            val intent: Intent
            if (!BuildConfig.HIDDEN_APP) {
                intent = Intent(context, MainActivity::class.java)
                builder
                    .setContentTitle(context.getString(R.string.settings_status_on_summary))
                    .setTicker(context.getString(R.string.settings_status_on_summary))
                    .color = ContextCompat.getColor(context, R.color.primary_dark)
            } else {
                intent = Intent(Settings.ACTION_SETTINGS)
            }
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, flags))
            return builder.build()
        }
    }
}