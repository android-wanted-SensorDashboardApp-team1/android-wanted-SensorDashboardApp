package com.preonboarding.sensordashboard.data.repository

import android.hardware.*
import androidx.room.withTransaction
import com.preonboarding.sensordashboard.data.dto.AxisData
import com.preonboarding.sensordashboard.data.room.SensorDataBase
import com.preonboarding.sensordashboard.di.AccSensorQualifier
import com.preonboarding.sensordashboard.di.GyroSensorQualifier
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.model.SensorType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val sensorManager: SensorManager,
    private val sensorDataBase: SensorDataBase,
    @AccSensorQualifier private val accSensor: Sensor,
    @GyroSensorQualifier private val gyroSensor: Sensor,
) {
    private lateinit var accTriggerEventListener: SensorEventListener
    private lateinit var gyroTriggerEventListener: SensorEventListener

    private val sensorDao = sensorDataBase.sensorDao()

    fun getAccFlow(block: (SensorData) -> Unit): SensorEventListener {
        return if (!this::accTriggerEventListener.isInitialized) {
            accTriggerEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let { sEvent ->
                        val array = sEvent.values

                        val sensorData = AxisData(
                            x = array[0],
                            y = array[1],
                            z = array[2],
                            type = SensorType.ACC
                        ).toSensorData()

                        block(sensorData)
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
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

    fun getGyroFlow(block: (SensorData) -> Unit): SensorEventListener {
        return if (!this::gyroTriggerEventListener.isInitialized) {
            gyroTriggerEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    //todo 드리프트 보상이 없는 무보정 x,y,z 값 리턴
                    //todo 추후에 보정된 값으로 변경 예정

                    event?.let { sEvent ->
                        val array = sEvent.values

                        val sensorData = AxisData(
                            x = array[0],
                            y = array[1],
                            z = array[2],
                            type = SensorType.GYRO
                        ).toSensorData()

                        block(sensorData)
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
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

    suspend fun insertSensorData(sensorData: SensorData) {
        sensorDataBase.withTransaction {
            sensorDao.insertSensorData(sensorData.toEntity())
        }
    }

    fun getSensorDataFlow(): Flow<SensorData> {
        return sensorDao.getSensorDataFlow().map { entity ->
            entity.toModel()
        }
    }

}