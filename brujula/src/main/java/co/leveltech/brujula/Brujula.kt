package co.leveltech.brujula

import android.content.Context
import android.util.Log
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.listener.OnBrujulaListener
import es.situm.sdk.SitumSdk
import es.situm.sdk.error.Error
import es.situm.sdk.location.LocationListener
import es.situm.sdk.location.LocationRequest
import es.situm.sdk.location.LocationStatus
import es.situm.sdk.model.location.Location

class Brujula {
    private var listener: OnBrujulaListener? = null
    private val locationRequest: LocationRequest by lazy {
        LocationRequest.Builder().build()
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("Brujula", location.toString())
            listener?.onEnterArea(Area(location.coordinate.latitude, location.coordinate.longitude, "Area from Gps"))
        }

        override fun onStatusChanged(locationStatus: LocationStatus) {
            Log.d("Brujula", locationStatus.toString())

        }

        override fun onError(error: Error) {
            Log.d("Brujula", error.toString())
        }
    }

    fun startPositioning() {
        SitumSdk.locationManager().requestLocationUpdates(locationRequest, locationListener)
    }

    fun stopPositioning() {
        SitumSdk.locationManager().removeUpdates()
    }

    fun getNearestAreas(): List<Area> {
        return nearestAreas
    }

    fun addOnBrujulaListener(listener: OnBrujulaListener) {
        this.listener = listener

    }

    private val nearestAreas = listOf(
        Area(50.4501, 30.5234, "Area 1"),
        Area(50.4501, 31.5234, "Area 2"),
        Area(50.4501, 32.5234, "Area 3")
    )

    companion object {
        private const val BRUJULA_GET_INSTANCE_ERROR_MSG =
            "Brujula was not initialized properly. Use Brujula.Builder to init library."

        private var instance: Brujula? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): Brujula {
            return if (instance != null) {
                instance!!
            } else {
                throw IllegalStateException(BRUJULA_GET_INSTANCE_ERROR_MSG)
            }
        }

        class Builder(
            private var context: Context
        ) {
            fun build() {
                instance = Brujula()
                SitumSdk.init(context)
                SitumSdk.configuration().isUseRemoteConfig = true
            }
        }
    }
}