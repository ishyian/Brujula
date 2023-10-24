package co.leveltech.brujula.network

import android.content.Context
import android.content.Intent
import com.unity3d.player.UnityPlayerActivity

internal class UnityHelper(private val context: Context) {

    fun openUnityGame() {
        context.startActivity(Intent(context, UnityPlayerActivity::class.java))
    }
}