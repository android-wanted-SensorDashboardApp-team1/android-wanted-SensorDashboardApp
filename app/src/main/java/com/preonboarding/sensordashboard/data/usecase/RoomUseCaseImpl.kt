package com.preonboarding.sensordashboard.data.usecase

import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import com.preonboarding.sensordashboard.domain.usecase.RoomUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomUseCaseImpl @Inject constructor(
    private val sensorRepository: SensorRepository
) : RoomUseCase {

    override suspend fun insertSensorData(sensorData: SensorData) {
        sensorRepository.insertSensorData(sensorData)
    }

    override fun getSensorDataFlow(): Flow<SensorData> {
        return sensorRepository.getSensorDataFlow()
    }
}