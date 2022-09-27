package com.preonboarding.sensordashboard.data.usecase

import android.hardware.TriggerEvent
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import kotlinx.coroutines.flow.Flow

class GyroSensorUseCaseImpl(private val repository: SensorRepository) : GyroSensorUseCase {

    override fun getGyroFlow(): Flow<TriggerEvent?> {
        return repository.getGyroFlow()
    }
}