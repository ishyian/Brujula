package co.leveltech.brujula.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

internal class PermissionHelper(private val context: Context, private val onPermissionGranted: () -> Unit) {

    private val requestPermissionLauncher =
        (context as AppCompatActivity).registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { granted: Map<String, Boolean> ->
            if (granted.containsValue(false).not()) {
                startLocationUpdates()
            } else {
                Log.d(TAG, "Location permission not granted")
            }
        }

    @SuppressLint("InlinedApi")
    fun startLocationUpdates() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }

            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    )
                )
            }
        }
    }

    companion object {
        private const val TAG = "PermissionHelper"
    }
}