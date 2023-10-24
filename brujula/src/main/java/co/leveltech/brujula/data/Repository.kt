package co.leveltech.brujula.data

import co.leveltech.brujula.data.response.LoginResponseModel
import co.leveltech.brujula.data.response.PrizesResponseModel
import co.leveltech.brujula.network.BrujulaApi
import io.reactivex.Observable

internal class Repository(
    private val api: BrujulaApi
) {
    fun login(tk: String, userId: String): Observable<LoginResponseModel> {
        return api.login(tk, userId)
    }

    fun checkPrizes(nonce: Int?): Observable<PrizesResponseModel> {
        return api.checkPrize(nonce)
    }
}