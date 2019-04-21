package com.sagarganatra.doordashsample.core

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Interface used for Injecting RxSchedulers through DI
 */
interface RxSchedulers {
    fun io(): Scheduler
    fun mainThread(): Scheduler
    fun computation(): Scheduler
}

class DefaultSchedulers: RxSchedulers {
    override fun io(): Scheduler  = Schedulers.io()

    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = Schedulers.computation()

}