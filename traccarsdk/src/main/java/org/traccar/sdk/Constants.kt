package org.traccar.sdk

class Constants {
    companion object {
        const val PRIMARY_CHANNEL = "default"
        private val TAG = Constants::class.java.simpleName
        private const val ALARM_MANAGER_INTERVAL = 15000
        const val KEY_DEVICE = "id"
        const val KEY_URL = "url"
        const val KEY_INTERVAL = "interval"
        const val KEY_DISTANCE = "distance"
        const val KEY_ANGLE = "angle"
        const val KEY_ACCURACY = "accuracy"
        const val KEY_STATUS = "status"
        const val KEY_BUFFER = "buffer"
        const val KEY_WAKELOCK = "wakelock"
        const val PERMISSIONS_REQUEST_LOCATION = 2
        const val PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 3
    }
}