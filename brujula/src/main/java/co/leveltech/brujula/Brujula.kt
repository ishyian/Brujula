package co.leveltech.brujula

import android.content.Context
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.Prize
import co.leveltech.brujula.listener.OnBrujulaListener

class Brujula {
    private var listener: OnBrujulaListener? = null

    fun getNearestAreas(): List<Area> {
        return nearestAreas
    }

    fun addOnBrujulaListener(listener: OnBrujulaListener) {
        this.listener = listener
        this.listener?.onEnterArea(enteredArea)
        this.listener?.onPrizeWin(mockPrize)
    }

    private val mockPrize = Prize("Prize 1")
    private val enteredArea = Area(50.4501, 30.5234, "Entered Area 1")

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