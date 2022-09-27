package com.preonboarding.sensordashboard.data.repository

import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener
import com.preonboarding.sensordashboard.data.LocalDataSource
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class SensorRepositoryImpl(
    private val localDataSource: LocalDataSource
) : SensorRepository {

    override fun getAccFlow(): Flow<TriggerEvent?> {
        return callbackFlow {
            var listener: TriggerEventListener? = localDataSource.getAccFlow { event ->
                trySend(event)
            }

            awaitClose { listener = null }
        }
    }

    override fun getGyroFlow(): Flow<TriggerEvent?> {
        return callbackFlow {
            var listener: TriggerEventListener? = localDataSource.getGyroFlow { event ->
                trySend(event)
            }

            awaitClose { listener = null }
        }
    }
}