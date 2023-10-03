package co.leveltech.brujula.listener

import co.leveltech.brujula.data.Area
import co.leveltech.brujula.data.Prize

interface OnBrujulaListener {
    fun onEnterArea(area: Area)
    fun onPrizeWin(prize: Prize)
}