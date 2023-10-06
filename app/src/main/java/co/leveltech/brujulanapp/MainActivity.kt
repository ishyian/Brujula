package co.leveltech.brujulanapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.leveltech.brujula.Brujula
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.Prize
import co.leveltech.brujula.listener.OnBrujulaListener
import co.leveltech.brujulan.R

class MainActivity : AppCompatActivity() {
    private val textLog by lazy {
        findViewById<TextView>(R.id.text_log)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { granted: Map<String, Boolean> ->
            if (granted.containsValue(false).not()) {
                startLocationUpdates()
            } else {
                Log.d(TAG, "Location permission not granted")
            }
        }

    private val listener = object : OnBrujulaListener {
        override fun onEnterArea(area: Area) {
            textLog.text = "${area.title} Location ${area.latitude} ${area.longitude}"
        }

        override fun onPrizeWin(prize: Prize) {
            textLog.text = "Prize win: ${prize.name}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Brujula.Companion.Builder(this).build()
        Brujula.getInstance().addOnBrujulaListener(listener)

        initClick()
        startLocationUpdates()
    }

    @SuppressLint("InlinedApi")
    private fun startLocationUpdates() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED -> {
                Brujula.getInstance().startPositioning()
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

    private fun initClick() {
        findViewById<Button>(R.id.btn_get_areas).setOnClickListener {
            val areas = Brujula.getInstance().getNearestAreas()
            textLog.text = areas
                .joinToString("\n") { "${it.title} Location: ${it.latitude} ${it.longitude}" }
        }

        findViewById<Button>(R.id.btn_enter_area).setOnClickListener {
            listener.onEnterArea(enteredArea)
        }

        findViewById<Button>(R.id.btn_prize_win).setOnClickListener {
            listener.onPrizeWin(mockPrize)
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
        private val mockPrize = Prize("Prize 1")
        private val enteredArea = Area(50.4501, 30.5234, "Entered Area 1")
    }
}