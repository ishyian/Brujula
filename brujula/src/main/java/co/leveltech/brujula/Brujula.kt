package co.leveltech.brujula

import android.content.Context
import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.Prize
import co.leveltech.brujula.listener.OnBrujulaListener

object Brujula {
    private var listener: OnBrujulaListener? = null

    fun getNearestAreas(): List<Area> {
        return nearestAreas
    }

    fun init(context: Context, listener: OnBrujulaListener) {
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
}