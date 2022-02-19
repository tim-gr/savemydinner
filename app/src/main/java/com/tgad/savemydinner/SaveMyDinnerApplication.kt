package com.tgad.savemydinner

import android.app.Application
import timber.log.Timber

class SaveMyDinnerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}