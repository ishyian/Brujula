package co.leveltech.brujula.data

import co.leveltech.brujula.network.BrujulaApi
import io.reactivex.Observable

internal class Repository(
    private val api: BrujulaApi
) {
    fun login(): Observable<String> {
        return Observable.fromCallable {
            "6db97e2ade7a18505bf9ada7caec9c91a59aafea455ab770109799472c03feeb"
        }
    }

    fun checkPrizes(): Observable<String> {
        return Observable.fromCallable {
            "prize found"
        }
    }
}