package co.leveltech.brujula.network

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.unity3d.player.UnityPlayerActivity
import es.situm.sdk.model.cartography.Geofence

internal class UnityHelper(private val context: Context) {

    fun openUnityGame() {
        context.startActivity(Intent(context, UnityPlayerActivity::class.java))
    }

    fun showGeofenceInfo(geofences: List<Geofence>) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Geofence Debug")
        builder.setMessage(geofences.toString())
        builder.setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}