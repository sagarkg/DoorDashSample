package com.sagarganatra.doordashsample.core

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulers: RxSchedulers {
    override fun io(): Scheduler = Schedulers.trampoline()

    override fun mainThread(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.io()
}