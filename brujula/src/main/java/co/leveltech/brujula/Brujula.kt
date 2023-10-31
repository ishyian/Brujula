package co.leveltech.brujula

import android.content.Context
import android.util.Log
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.PermissionHelper
import co.leveltech.brujula.data.Repository
import co.leveltech.brujula.data.response.LoginResponseModel
import co.leveltech.brujula.extensions.async
import co.leveltech.brujula.network.RetrofitHelper
import co.leveltech.brujula.network.UnityHelper
import co.leveltech.brujula.view.BrujulaMapView
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
import es.situm.sdk.wayfinding.MapView
import es.situm.sdk.wayfinding.MapViewConfiguration
import es.situm.sdk.wayfinding.MapViewController
import io.reactivex.disposables.CompositeDisposable
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Brujula {
    private var disposables = CompositeDisposable()
    private var repository: Repository? = null
    private var buildingId: String = "14582"
    private var fullName = ""
    private var userId = ""
    private var apiToken = "5ace1d6372cb69f5132ad7e3662e2905709893e7dbd7b3cc4539fdf01b3c619c"
    private var unityHelper: UnityHelper? = null
    private var permissionHelper: PermissionHelper? = null
    private var isMapVisible = false

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
                if (isMapVisible) enterZone()
                if (DEBUG_GEOFENCE) {
                    unityHelper?.showGeofenceInfo(it)
                }
            }
        }

        override fun onExitedGeofences(geofences: MutableList<Geofence>?) {
            //Nothing
        }
    }

    private fun startPositioning() {
        SitumSdk.locationManager().requestLocationUpdates(locationRequest, locationListener)
    }

    private fun stopPositioning() {
        SitumSdk.locationManager().removeUpdates()
    }

    fun configureMapView(mapView: BrujulaMapView) {
        val configuration: MapViewConfiguration = MapViewConfiguration.Builder()
            .setBuildingIdentifier(buildingId).build()
        mapView.map.load(configuration, object : MapView.MapViewCallback {
            override fun onLoad(mapViewController: MapViewController) {
                Log.d(TAG, "onLoad")
            }

            override fun onError(error: Error) {
                Log.e(TAG, "configureMapView $error")
            }
        })
    }

    fun onResume() {
        instance?.isMapVisible = true
        permissionHelper?.startLocationUpdates()
    }

    fun onStop() {
        instance?.isMapVisible = false
        instance?.stopPositioning()
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

    fun enterZone(): Boolean {
        if (isMapVisible) unityHelper?.openUnityGame()
        return isMapVisible
    }

    private fun loginIntoSitumSdk() {
        disposables.add(
            repository!!.login(apiToken, userId)
                .async()
                .subscribe(::onLoginIntoSitumSuccess, ::onLoginIntoSitumError)
        )
    }

    private fun onLoginIntoSitumSuccess(model: LoginResponseModel) {
        SitumSdk.configuration().setApiKey(model.ips.user, model.ips.token)
        SitumSdk.configuration().isUseRemoteConfig = true
        instance?.buildingId = model.ips.building
        SitumSdk.locationManager().setGeofenceListener(geofenceListener)
        Log.d(TAG, "Login success")
    }

    private fun onLoginIntoSitumError(throwable: Throwable) {
        Log.e(TAG, "Login error", throwable)
    }

    companion object {
        const val DEBUG_GEOFENCE = false
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
            private val apiToken: String?
        ) {
            fun build() {
                instance = Brujula()
                val retrofit = RetrofitHelper()
                instance!!.repository =
                    Repository(retrofit.api)
                instance!!.userId = userId
                instance!!.fullName = fullName
                if (apiToken.isNullOrEmpty().not()) {
                    instance!!.apiToken = requireNotNull(apiToken)
                }
                instance?.unityHelper = UnityHelper(context)
                instance?.permissionHelper = PermissionHelper(context) {
                    instance?.startPositioning()
                }
                SitumSdk.init(context)
                instance?.loginIntoSitumSdk()
            }
        }
    }
}