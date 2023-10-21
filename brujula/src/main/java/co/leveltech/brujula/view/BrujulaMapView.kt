package co.leveltech.brujula.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import co.leveltech.brujula.R
import es.situm.sdk.wayfinding.MapView

class BrujulaMapView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    lateinit var map: MapView

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.view_brujula_map, this)
        map = findViewById(R.id.map_view)
    }
}