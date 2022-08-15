package org.traccar.sdk

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceManager
import androidx.preference.TwoStatePreference
import java.util.*
import kotlin.collections.HashSet

class Root {
    companion object {
         fun startTrackingService(activity: Activity, context: Context, checkPermission: Boolean, initialPermission: Boolean) {
            var permission = initialPermission

            if (checkPermission) {
                val requiredPermissions: MutableSet<String> = HashSet()
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                }
                permission = requiredPermissions.isEmpty()
                if (!permission) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        activity.requestPermissions(requiredPermissions.toTypedArray(), Constants.PERMISSIONS_REQUEST_LOCATION)
                    }
                    return
                }
            }
            if (permission) {
                //TODO: Verify
//                setPreferencesEnabled(false)
                ContextCompat.startForegroundService(context, Intent(activity, TrackingService::class.java))
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
//                    alarmManager.setInexactRepeating(
//                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                        ALARM_MANAGER_INTERVAL.toLong(), ALARM_MANAGER_INTERVAL.toLong(), alarmIntent
//                    )
//                }
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
//                    && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    requestingPermissions = true
//                    showBackgroundLocationDialog(requireContext()) {
//                        requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), PERMISSIONS_REQUEST_BACKGROUND_LOCATION)
//                    }
//                } else {
//                    requestingPermissions = BatteryOptimizationHelper().requestException(requireContext())
//                }
            } else {
//                sharedPreferences.edit().putBoolean(KEY_STATUS, false).apply()
//                val preference = findPreference<TwoStatePreference>(KEY_STATUS)
//                preference?.isChecked = false
            }
        }

         fun stopTrackingService(activity: Activity) {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
//                alarmManager.cancel(alarmIntent)
//            }
            activity.stopService(Intent(activity, TrackingService::class.java))
//            setPreferencesEnabled(true)
        }

        fun init(activity: Activity){
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            if (!sharedPreferences.contains(Constants.KEY_DEVICE)) {
                val id = (Random().nextInt(900000) + 100000).toString()
                sharedPreferences.edit().putString(Constants.KEY_DEVICE, id).apply()
            }
        }

        fun getDeviceKey(activity: Activity): String? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            return sharedPreferences.getString(Constants.KEY_DEVICE, "undefined")
        }

        fun setDeviceKey(activity: Activity, id: String) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            sharedPreferences.edit().putString(Constants.KEY_DEVICE, id).apply()
        }
    }

}