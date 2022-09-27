package com.preonboarding.sensordashboard.data.repository

import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener
import com.preonboarding.sensordashboard.di.AccSensorQualifier
import com.preonboarding.sensordashboard.di.GyroSensorQualifier
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val sensorManager: SensorManager,
    @AccSensorQualifier private val accSensor: Sensor,
    @GyroSensorQualifier private val gyroSensor: Sensor,
) {
    lateinit var accTriggerEventListener: TriggerEventListener
    lateinit var gyroTriggerEventListener: TriggerEventListener

    fun getAccFlow(block: (TriggerEvent?) -> Unit): TriggerEventListener {
        return if (!this::accTriggerEventListener.isInitialized) {
            accTriggerEventListener = object : TriggerEventListener() {
                override fun onTrigger(event: TriggerEvent?) {
                    block(event)
                }
            }
            sensorManager.requestTriggerSensor(accTriggerEventListener, accSensor)

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
            sensorManager.requestTriggerSensor(gyroTriggerEventListener, gyroSensor)

            gyroTriggerEventListener

        } else {
            gyroTriggerEventListener
        }
    }
}