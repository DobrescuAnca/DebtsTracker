package com.debts.debtstracker

import android.app.Application
import android.content.Context
import com.debts.debtstracker.injection.modulesList
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DebtsTrackerApplication: Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DebtsTrackerApplication)
            modules(modulesList)
        }
    }

    companion object {
        private var instance: DebtsTrackerApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun applicationInstance(): DebtsTrackerApplication{
            return instance!!
        }
    }
}