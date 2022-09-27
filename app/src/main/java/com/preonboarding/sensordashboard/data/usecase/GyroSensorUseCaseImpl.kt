package com.preonboarding.sensordashboard.data.usecase

import android.hardware.TriggerEvent
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GyroSensorUseCaseImpl @Inject constructor(
    private val repository: SensorRepository
) : GyroSensorUseCase {

    override fun getGyroFlow(): Flow<TriggerEvent?> {
        return repository.getGyroFlow()
    }
}