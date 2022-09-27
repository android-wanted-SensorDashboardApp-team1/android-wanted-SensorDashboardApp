package com.preonboarding.sensordashboard

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SensorApplication :Application() {

    override fun onCreate() {
        super.onCreate()
    }
}