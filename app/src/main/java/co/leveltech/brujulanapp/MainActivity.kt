package co.leveltech.brujulanapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import co.leveltech.brujula.Brujula
import co.leveltech.brujulan.R
import co.leveltech.brujulanapp.helloworld.HelloWorldFragment
import co.leveltech.brujulanapp.map.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val userId = "johnDoe"
    private val fullName = "John Doe"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Brujula.Companion.Builder(
            context = this,
            userId = userId,
            fullName = fullName,
            apiToken = null
        ).build()

        setCurrentFragment(HelloWorldFragment())

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.hello_world -> setCurrentFragment(HelloWorldFragment())
                R.id.map -> setCurrentFragment(MapFragment())
            }
            true
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