package co.leveltech.brujula

import android.content.Context
import android.util.Log
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.listener.OnBrujulaListener
import es.situm.sdk.SitumSdk
import es.situm.sdk.error.Error
import es.situm.sdk.location.GeofenceListener
import es.situm.sdk.location.LocationListener
import es.situm.sdk.location.LocationRequest
import es.situm.sdk.location.LocationStatus
import es.situm.sdk.model.cartography.Geofence
import es.situm.sdk.model.location.Location
import io.reactivex.disposables.CompositeDisposable

class Brujula {
    private var disposables = CompositeDisposable()
    private var listener: OnBrujulaListener? = null
    private val locationRequest: LocationRequest by lazy {
        LocationRequest.Builder().build()
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("Brujula", location.toString())
        }

        override fun onStatusChanged(locationStatus: LocationStatus) {
            Log.d("Brujula", locationStatus.toString())
        }

        override fun onError(error: Error) {
            Log.d("Brujula", error.toString())
        }
    }

    private val geofenceListener = object : GeofenceListener {
        override fun onEnteredGeofences(geofences: MutableList<Geofence>?) {
            Log.d("Brujula", "onEnteredGeofences")
            geofences?.let {
                listener?.onEnterArea(Area(geofences))
            }
        }

        override fun onExitedGeofences(geofences: MutableList<Geofence>?) {
            Log.d("Brujula", "onExitedGeofences")
        }
    }

    fun startPositioning() {
        SitumSdk.locationManager().requestLocationUpdates(locationRequest, locationListener)
        SitumSdk.locationManager().setGeofenceListener(geofenceListener)
    }

    fun loginIntoSitumSdk() {

    }

    fun stopPositioning() {
        SitumSdk.locationManager().removeUpdates()
    }

    fun getNearestAreas(): List<Geofence> {
        return emptyList()
    }

    fun addOnBrujulaListener(listener: OnBrujulaListener) {
        this.listener = listener

    }

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
                SitumSdk.configuration()
                    .setApiKey("email@email.com", "6db97e2ade7a18505bf9ada7caec9c91a59aafea455ab770109799472c03feeb")
                SitumSdk.configuration().isUseRemoteConfig = true
            }
        }
    }
}