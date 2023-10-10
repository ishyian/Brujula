package co.leveltech.brujula.network

import co.leveltech.brujula.data.response.LoginResponseModel
import co.leveltech.brujula.data.response.PrizesResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

internal interface BrujulaApi {
    @GET("login.json")
    fun login(): Observable<LoginResponseModel>

    @GET("checkPrize.json")
    fun checkPrize(
        @Query("nonce") nonce: Int? = null
    ): Observable<PrizesResponseModel>
}