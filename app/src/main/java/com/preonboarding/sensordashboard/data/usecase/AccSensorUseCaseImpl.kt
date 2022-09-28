package com.preonboarding.sensordashboard.data.usecase

import android.hardware.SensorEvent
import com.preonboarding.sensordashboard.di.IoDispatcher
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AccSensorUseCaseImpl @Inject constructor(
    private val repository: SensorRepository
) : AccSensorUseCase {

    override fun getAccFlow(): Flow<SensorEvent?> {
        return repository.getAccFlow()
    }
}