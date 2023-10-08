package co.leveltech.brujula.extensions

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal fun <T : Any> Observable<T>.async(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

internal fun <T : Any> Single<T>.async(): Single<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

internal fun <T> T.just(): Observable<T> {
    return Observable.just(this)
}

internal fun <T> Observable<T>.item(): T {
    return this.blockingFirst()
}