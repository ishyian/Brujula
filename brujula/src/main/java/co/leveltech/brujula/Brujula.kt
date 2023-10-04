package co.leveltech.brujula

import android.content.Context
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.listener.OnBrujulaListener

class Brujula {
    private var listener: OnBrujulaListener? = null

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
            }
        }
    }
}