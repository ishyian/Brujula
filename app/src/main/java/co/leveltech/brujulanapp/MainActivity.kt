package co.leveltech.brujulanapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import co.leveltech.brujula.Brujula
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.Prize
import co.leveltech.brujula.listener.OnBrujulaListener
import co.leveltech.brujulan.R

class MainActivity : AppCompatActivity() {

    private val textLog by lazy {
        findViewById<TextView>(R.id.text_log)
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