package com.preonboarding.sensordashboard.data.repository

import android.hardware.*
import com.preonboarding.sensordashboard.di.AccSensorQualifier
import com.preonboarding.sensordashboard.di.GyroSensorQualifier
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val sensorManager: SensorManager,
    @AccSensorQualifier private val accSensor: Sensor,
    @GyroSensorQualifier private val gyroSensor: Sensor,
) {
    private lateinit var accTriggerEventListener: SensorEventListener
    private lateinit var gyroTriggerEventListener: SensorEventListener

    fun getAccFlow(block: (SensorEvent?) -> Unit): SensorEventListener {
        return if (!this::accTriggerEventListener.isInitialized) {
            accTriggerEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    block(event)
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

                }
            }
            sensorManager.registerListener(
                accTriggerEventListener,
                accSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            accTriggerEventListener

        } else {
            accTriggerEventListener
        }
    }

    fun getGyroFlow(block: (SensorEvent?) -> Unit): SensorEventListener {
        return if (!this::gyroTriggerEventListener.isInitialized) {
            gyroTriggerEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    block(event)
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

                }
            }
            sensorManager.registerListener(
                gyroTriggerEventListener,
                gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            gyroTriggerEventListener

        } else {
            gyroTriggerEventListener
        }
    }
}