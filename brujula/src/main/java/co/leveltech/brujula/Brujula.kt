package co.leveltech.brujula

import android.content.Context
import android.util.Log
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.Repository
import co.leveltech.brujula.extensions.async
import co.leveltech.brujula.listener.OnBrujulaListener
import co.leveltech.brujula.network.RetrofitHelper
import es.situm.sdk.SitumSdk
import es.situm.sdk.error.Error
import es.situm.sdk.location.GeofenceListener
import es.situm.sdk.location.LocationListener
import es.situm.sdk.location.LocationRequest
import es.situm.sdk.location.LocationStatus
import es.situm.sdk.model.cartography.Geofence
import es.situm.sdk.model.location.Location
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class Brujula {
    private var disposables = CompositeDisposable()
    private var listener: OnBrujulaListener? = null
    private var repository: Repository? = null

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.Builder().build()
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //Nothing
        }

        override fun onStatusChanged(locationStatus: LocationStatus) {
            //Nothing
        }

        override fun onError(error: Error) {
            //Nothing
        }
    }

    private val geofenceListener = object : GeofenceListener {
        override fun onEnteredGeofences(geofences: MutableList<Geofence>?) {
            geofences?.let {
                listener?.onEnterArea(Area(geofences))
            }
        }

        override fun onExitedGeofences(geofences: MutableList<Geofence>?) {
            //Nothing
        }
    }

    fun startPositioning() {
        SitumSdk.locationManager().requestLocationUpdates(locationRequest, locationListener)
        SitumSdk.locationManager().setGeofenceListener(geofenceListener)
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

    private fun loginIntoSitumSdk() {
        disposables.add(
            repository!!.login()
                .async()
                .subscribe(::onLoginIntoSitumSuccess, ::onLoginIntoSitumError)
        )
    }

    private fun onLoginIntoSitumSuccess(apiKey: String) {
        SitumSdk.configuration().setApiKey("email@email.com", apiKey)
        SitumSdk.configuration().isUseRemoteConfig = true
        Log.d(TAG, "Login success")
    }

    private fun onLoginIntoSitumError(throwable: Throwable) {
        Log.e(TAG, "Login error", throwable)
    }

    private fun checkPrizes() {
        disposables.add(
            repository!!.checkPrizes()
                .async()
                .repeatWhen { observable ->
                    observable.delay(60, TimeUnit.SECONDS)
                }
                .subscribe(
                    ::onCheckPrizesSuccess, ::onCheckPrizesError
                )
        )
    }

    private fun onCheckPrizesSuccess(result: String) {

    }

    private fun onCheckPrizesError(result: Throwable) {

    }

    companion object {
        private const val BRUJULA_GET_INSTANCE_ERROR_MSG =
            "Brujula was not initialized properly. Use Brujula.Builder to init library."
        private const val TAG =
            "BrujulaSdk"

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
                val retrofit = RetrofitHelper()
                instance!!.repository =
                    Repository(retrofit.api)
                SitumSdk.init(context)
                instance?.loginIntoSitumSdk()
                instance?.checkPrizes()
            }
        }
    }
}