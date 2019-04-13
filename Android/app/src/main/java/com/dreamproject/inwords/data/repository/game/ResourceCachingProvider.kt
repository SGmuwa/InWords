package com.dreamproject.inwords.data.repository.game

import android.annotation.SuppressLint
import android.util.Log
import com.dreamproject.inwords.domain.model.Resource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * @param databaseInserter not wrapped as resource: calling onError if error
 * @param databaseGetter not wrapped as resource: calling onError if no items
 * @param remoteDataProvider same rules
 */
internal class ResourceCachingProvider<T : Any>(
        private val databaseInserter: (T) -> Single<T>, //should be interface getAll insertAll only
        private val databaseGetter: () -> Single<T>, //should be interface getAll insertAll only
        private val remoteDataProvider: () -> Single<T>) {

    private val askForContentStream: Subject<Unit> = PublishSubject.create()
    private val fakeRemoteStream: Subject<Resource<T>> = PublishSubject.create()

    private val resourceStream: Subject<Resource<T>> = BehaviorSubject.create()

    init {
        askForContentStream
                .flatMap { fakeRemoteStream.mergeWith(remoteDataProvider().wrapResource()) }
                .flatMapSingle { res ->
                    if (res.success()) {
                        databaseInserter(res.data!!)
                                .doOnError { Log.d(TAG, it.message) }
                                .wrapResourceOverwriteError(res)
                    }

                    Single.just(res)
                }
                .switchMapSingle {
                    if (it.success()) {
                        Single.just(it)
                    } else {
                        Log.d(TAG, it.message)
                        databaseGetter().wrapResource()
                    }
                }
                .subscribe(resourceStream)

        askForContentUpdate()
    }

    fun askForContentUpdate() {
        askForContentStream.onNext(Unit)
    }

    fun postOnLoopback(value: T) {
        fakeRemoteStream.onNext(Resource.success(value))
    }

    fun observe(): Observable<Resource<T>> = resourceStream

    private fun Single<T>.wrapResource(): Single<Resource<T>> {
        return map { Resource.success(it) }
                .onErrorReturn { Resource.error(it.message, null) }
    }

    private fun Single<T>.wrapResourceOverwriteError(onErrorResource: Resource<T>): Single<Resource<T>> {
        return map { Resource.success(it) }
                .onErrorReturn { onErrorResource }
    }

    @SuppressLint("UseSparseArrays")
    class Locator<T : Any> {
        private val cachingProvidersMap = HashMap<Int, ResourceCachingProvider<T>>()

        fun get(id: Int, factory: (Int) -> ResourceCachingProvider<T>): ResourceCachingProvider<T> {
            return cachingProvidersMap[id] ?: factory(id)
        }
    }

    companion object {
        const val TAG: String = "ResourceCachingProvider"
    }
}