package co.leveltech.brujulanapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.leveltech.brujula.Brujula
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.Prize
import co.leveltech.brujula.listener.OnBrujulaListener
import co.leveltech.brujulan.R
import co.leveltech.brujulanapp.helloworld.HelloWorldFragment
import co.leveltech.brujulanapp.map.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

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

    private val currentFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.container) as Fragment
    }

    private val userId = "johnDoe"
    private val fullName = "John Doe"
    private val apiKey = "apiKey"

    private val listener = object : OnBrujulaListener {
        override fun onEnterArea(area: Area) {
            (currentFragment as? HelloWorldFragment)?.onEnterArea(area)
        }

        override fun onPrizeWin(prize: Prize) {
            (currentFragment as? HelloWorldFragment)?.onPrizeWin(prize)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Brujula.Companion.Builder(
            context = this,
            userId = userId,
            fullName = fullName,
            apiToken = apiKey
        ).build()

        Brujula.getInstance().addOnBrujulaListener(listener)
        startLocationUpdates()

        setCurrentFragment(HelloWorldFragment())

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.hello_world -> setCurrentFragment(HelloWorldFragment())
                R.id.map -> setCurrentFragment(MapFragment())
            }
            true
        }
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

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}