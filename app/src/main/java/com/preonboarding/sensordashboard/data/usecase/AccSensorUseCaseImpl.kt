package com.preonboarding.sensordashboard.data.usecase

import android.hardware.TriggerEvent
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccSensorUseCaseImpl @Inject constructor(
    private val repository: SensorRepository
) : AccSensorUseCase {

    override fun getAccFlow(): Flow<TriggerEvent?> {
        return repository.getAccFlow()
    }
}