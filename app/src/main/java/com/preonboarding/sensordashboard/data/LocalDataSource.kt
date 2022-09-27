package com.preonboarding.sensordashboard.data

import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener

class LocalDataSource {
    lateinit var accTriggerEventListener: TriggerEventListener
    lateinit var gyroTriggerEventListener: TriggerEventListener

    fun getAccFlow(block: (TriggerEvent?) -> Unit): TriggerEventListener {
        return if (!this::accTriggerEventListener.isInitialized) {
            accTriggerEventListener = object : TriggerEventListener() {
                override fun onTrigger(event: TriggerEvent?) {
                    block(event)
                }
            }
            accTriggerEventListener

        } else {
            accTriggerEventListener
        }
    }

    fun getGyroFlow(block: (TriggerEvent?) -> Unit): TriggerEventListener {
        return if (!this::gyroTriggerEventListener.isInitialized) {
            gyroTriggerEventListener = object : TriggerEventListener() {
                override fun onTrigger(event: TriggerEvent?) {
                    block(event)
                }
            }
            gyroTriggerEventListener

        } else {
            gyroTriggerEventListener
        }
    }
}