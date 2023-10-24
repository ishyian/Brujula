package co.leveltech.brujulanapp.helloworld

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import co.leveltech.brujula.Brujula
import co.leveltech.brujulan.R
import kotlinx.coroutines.launch

class HelloWorldFragment : Fragment(R.layout.fragment_hello_world) {
    private val textLog by lazy {
        requireNotNull(view).findViewById<TextView>(R.id.text_log)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick(view)
    }

    private fun initClick(view: View) {
        view.findViewById<Button>(R.id.btn_get_areas).setOnClickListener {
            textLog.text = "Loading nearest areas..."
            lifecycleScope.launch {
                val areas = Brujula.getInstance().getNearestAreas()
                textLog.text = areas.map { it.geofence }.toString()
            }
        }

        view.findViewById<Button>(R.id.btn_enter_area).setOnClickListener {
            Brujula.getInstance().enterZone()
        }
    }
}