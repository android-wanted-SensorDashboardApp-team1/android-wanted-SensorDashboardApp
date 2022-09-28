package com.preonboarding.sensordashboard.domain.usecase

import com.preonboarding.sensordashboard.domain.model.SensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RoomUseCase {

    suspend fun insertSensorData( sensorData: SensorData)

    fun getSensorDataFlow(): Flow<SensorData>
}