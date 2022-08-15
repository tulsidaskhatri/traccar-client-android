package org.traccar.client

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import org.traccar.sdk.Constants
import org.traccar.sdk.Root
import org.traccar.sdk.TrackingService

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        val initButton = findViewById<Button>(R.id.initButton)
        val getDeviceIDButton = findViewById<Button>(R.id.getDeviceIDButton)


        startButton.setOnClickListener {
            Root.startTrackingService(this, this, true, false)

        }
        stopButton.setOnClickListener {

            Root.stopTrackingService(this)
        }

        initButton.setOnClickListener {
            Root.init(this)
        }

        getDeviceIDButton.setOnClickListener {
            val deviceId = Root.getDeviceKey(this);
            if (deviceId != null) {
                Log.d("DeviceShow",deviceId )
            } else {
                Log.d("DeviceShow", "No device to show")
            }
        }



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSIONS_REQUEST_LOCATION) {
            var granted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false
                    break
                }
            }
            Root.startTrackingService(this, this, false, granted)
        }
    }
}