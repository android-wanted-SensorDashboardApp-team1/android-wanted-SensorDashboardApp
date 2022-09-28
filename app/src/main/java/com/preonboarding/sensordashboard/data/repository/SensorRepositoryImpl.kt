package com.preonboarding.sensordashboard.data.repository

import android.hardware.SensorEventListener
import com.preonboarding.sensordashboard.di.coroutine.SensorScopeQualifier
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    @SensorScopeQualifier private val coroutineScope: CoroutineScope
) : SensorRepository {

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

    override fun getAccFlow(): Flow<SensorData> {
        return callbackFlow {
            var listener: SensorEventListener? = localDataSource.getAccFlow { sensorData ->
                sensorScope.launch {
                    send(sensorData)
                }
            }

            awaitClose { listener = null }
        }
    }

    override fun getGyroFlow(): Flow<SensorData> {
        return callbackFlow {
            var listener: SensorEventListener? = localDataSource.getGyroFlow { sensorData ->
                sensorScope.launch {
                    send(sensorData)
                }
            }

            awaitClose { listener = null }
        }
    }

    override fun errorFlow(): MutableSharedFlow<Throwable> {
        return errorFlow
    }

    override suspend fun insertSensorData(sensorData: SensorData) {
        localDataSource.insertSensorData(sensorData)
    }

    override fun getSensorDataFlow(): Flow<SensorData> {
        return localDataSource.getSensorDataFlow()
    }
}