package co.leveltech.brujulanapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.leveltech.brujula.Brujula
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.Prize
import co.leveltech.brujula.listener.OnBrujulaListener
import co.leveltech.brujulan.R

class MainActivity : AppCompatActivity(), OnBrujulaListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Brujula.Companion.Builder(this).build()

        Brujula.getInstance().addOnBrujulaListener(object : OnBrujulaListener {
            override fun onEnterArea(area: Area) {
                //Area
            }

            override fun onPrizeWin(prize: Prize) {
                //Prize
            }
        })

        val areas = Brujula.getInstance().getNearestAreas()
    }

    override fun onEnterArea(area: Area) {
        Log.d(TAG, area.toString())
    }

    override fun onPrizeWin(prize: Prize) {
        Log.d(TAG, prize.toString())
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}