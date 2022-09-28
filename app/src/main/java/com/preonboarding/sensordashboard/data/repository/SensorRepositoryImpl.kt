package com.preonboarding.sensordashboard.data.repository

import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.preonboarding.sensordashboard.di.SensorScopeQualifier
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

    override fun getAccFlow(): Flow<SensorEvent?> {
        return callbackFlow {
            var listener: SensorEventListener? = localDataSource.getAccFlow { event ->
                sensorScope.launch {
                    send(event)
                }
            }

            awaitClose { listener = null }
        }
    }

    override fun getGyroFlow(): Flow<SensorEvent?> {
        return callbackFlow {
            var listener: SensorEventListener? = localDataSource.getGyroFlow { event ->
                sensorScope.launch {
                    send(event)
                }
            }

            awaitClose { listener = null }
        }
    }

    override fun errorFlow(): MutableSharedFlow<Throwable> {
        return errorFlow
    }
}