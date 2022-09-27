package com.preonboarding.sensordashboard.data.repository

import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(
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