package co.leveltech.brujula

import android.content.Context
import android.util.Log
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.Prize
import co.leveltech.brujula.data.Repository
import co.leveltech.brujula.data.response.LoginResponseModel
import co.leveltech.brujula.data.response.PrizesResponseModel
import co.leveltech.brujula.extensions.async
import co.leveltech.brujula.listener.OnBrujulaListener
import co.leveltech.brujula.network.RetrofitHelper
import es.situm.sdk.SitumSdk
import es.situm.sdk.error.Error
import es.situm.sdk.location.GeofenceListener
import es.situm.sdk.location.LocationListener
import es.situm.sdk.location.LocationRequest
import es.situm.sdk.location.LocationStatus
import es.situm.sdk.model.cartography.Building
import es.situm.sdk.model.cartography.Geofence
import es.situm.sdk.model.location.Location
import es.situm.sdk.utils.Handler
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Brujula {
    private var disposables = CompositeDisposable()
    private var listener: OnBrujulaListener? = null
    private var repository: Repository? = null
    private var nonce: Int? = null
    private var buildingId: String = "14582"
    private var fullName = ""
    private var userId = ""
    private var apiToken = ""

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

    suspend fun getNearestAreas(): List<Area> = suspendCoroutine { continuation ->
        val building = Building.Builder().identifier(buildingId).build()
        SitumSdk.communicationManager().fetchGeofencesFromBuilding(building, object : Handler<List<Geofence>> {
            override fun onSuccess(geofences: List<Geofence>?) {
                Log.d(TAG, "onSuccessGetNearestAreas ${geofences?.size}")
                return continuation.resume(geofences?.map {
                    Area(listOf(it))
                } ?: emptyList())
            }

            override fun onFailure(error: Error?) {
                return continuation.resume(emptyList())
            }
        })
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

    private fun onLoginIntoSitumSuccess(model: LoginResponseModel) {
        SitumSdk.configuration().setApiKey(model.ips.user, model.ips.token)
        SitumSdk.configuration().isUseRemoteConfig = true
        Log.d(TAG, "Login success")
    }

    private fun onLoginIntoSitumError(throwable: Throwable) {
        Log.e(TAG, "Login error", throwable)
    }

    private fun checkPrizes() {
        if (nonce != null) {
            nonce = requireNotNull(nonce) + 1
        }
        disposables.add(
            repository!!.checkPrizes(nonce)
                .async()
                .repeatWhen { observable ->
                    observable.delay(60, TimeUnit.SECONDS)
                }
                .subscribe(
                    ::onCheckPrizesSuccess, ::onCheckPrizesError
                )
        )
    }

    private fun onCheckPrizesSuccess(model: PrizesResponseModel) {
        nonce = model.nonce
        if (model.prizes.isNotEmpty()) {
            val prize = model.prizes.first()
            listener?.onPrizeWin(
                Prize(
                    id = prize.id ?: "",
                    name = prize.name ?: "",
                    received = prize.received
                )
            )
        }
    }

    private fun onCheckPrizesError(result: Throwable) {
        Log.d(TAG, "onCheckPrizesError", result)
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
            private val context: Context,
            private val userId: String,
            private val fullName: String,
            private val apiToken: String
        ) {
            fun build() {
                instance = Brujula()
                val retrofit = RetrofitHelper()
                instance!!.repository =
                    Repository(retrofit.api)
                instance!!.userId = userId
                instance!!.fullName = fullName
                instance!!.apiToken = apiToken
                SitumSdk.init(context)
                instance?.loginIntoSitumSdk()
                instance?.checkPrizes()
            }
        }
    }
}