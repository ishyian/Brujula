package co.leveltech.brujulanapp.map

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import co.leveltech.brujula.Brujula
import co.leveltech.brujula.view.BrujulaMapView
import co.leveltech.brujulan.R

class MapFragment : Fragment(R.layout.fragment_map) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapView = view.findViewById<BrujulaMapView>(R.id.brujulaMapView)
        Brujula.getInstance().configureMapView(mapView)

        view.findViewById<Button>(R.id.btn_enter_area).setOnClickListener {
            Brujula.getInstance().enterZone(null)
        }
    }

    override fun onResume() {
        super.onResume()
        Brujula.getInstance().onResume()
    }

    override fun onStop() {
        super.onStop()
        Brujula.getInstance().onStop()
    }
}