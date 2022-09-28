package com.preonboarding.sensordashboard.data.repository

import android.hardware.*
import androidx.room.withTransaction
import com.preonboarding.sensordashboard.data.dto.AxisData
import com.preonboarding.sensordashboard.data.room.SensorDataBase
import com.preonboarding.sensordashboard.di.coroutine.SensorScopeQualifier
import com.preonboarding.sensordashboard.di.sensor.AccSensorQualifier
import com.preonboarding.sensordashboard.di.sensor.GyroSensorQualifier
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.model.SensorType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val sensorManager: SensorManager,
    private val sensorDataBase: SensorDataBase,
    @AccSensorQualifier private val accSensor: Sensor,
    @GyroSensorQualifier private val gyroSensor: Sensor,
    @SensorScopeQualifier private val coroutineScope: CoroutineScope
) {
    private val errorFlow = MutableSharedFlow<Throwable>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val ceh = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch {
            errorFlow.emit(throwable)
        }
    }

    private val sensorScope = coroutineScope + ceh

    private val sensorDao = sensorDataBase.sensorDao()

    fun getAccFlow(): Flow<SensorData> {
        return callbackFlow {
            var listener: SensorEventListener? = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let { sEvent ->
                        val array = sEvent.values

                        val sensorData = AxisData(
                            x = array[0],
                            y = array[1],
                            z = array[2],
                            type = SensorType.ACC
                        ).toSensorData()

                        sensorScope.launch {
                            send(sensorData)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                listener,
                accSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            awaitClose { listener = null }
        }
    }

    fun getGyroFlow(): Flow<SensorData> {
        return callbackFlow {
            var listener: SensorEventListener? = object : SensorEventListener {
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

                        sensorScope.launch {
                            send(sensorData)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                listener,
                gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            awaitClose { listener = null }
        }
    }

    suspend fun insertSensorData(sensorData: SensorData) {
        sensorDataBase.withTransaction {
            sensorDao.insertSensorData(sensorData.toEntity())
        }
    }

    fun getSensorDataFlow(): Flow<List<SensorData>> {
        return sensorDao.getSensorDataFlow().map { list ->
            list.map {
                it.toModel()
            }
        }
    }

    fun getErrorFlow(): MutableSharedFlow<Throwable> {
        return errorFlow
    }

}