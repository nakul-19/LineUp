package com.gdsc.lineup

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * @Author: Karan Verma
 * @Date: 26/04/22
 */

@HiltAndroidApp
class LineUpApp : Application(){
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(applicationContext)

    }

}