package co.leveltech.brujula.network

import co.leveltech.brujula.BuildConfig
import co.leveltech.brujula.extensions.UnsafeOkHttpClient
import com.google.gson.Gson
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class RetrofitHelper {
    val api by lazy {
        getRetrofitInstance().create(BrujulaApi::class.java)
    }

    private fun getRetrofitInstance(): Retrofit {
        val httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        return Retrofit.Builder()
            .client(httpClient.build())
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    Gson().newBuilder().setLenient().create()
                )
            )
            .build()
    }

    private val baseUrl = "https://brujula.level-tech.co/"
}