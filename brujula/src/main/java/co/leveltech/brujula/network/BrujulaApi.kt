package co.leveltech.brujula.network

import retrofit2.http.GET

interface BrujulaApi {
    @GET("login")
    fun login()
}