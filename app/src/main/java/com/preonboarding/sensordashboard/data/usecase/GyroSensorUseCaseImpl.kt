package com.preonboarding.sensordashboard.data.usecase

import android.hardware.SensorEvent
import com.preonboarding.sensordashboard.di.IoDispatcher
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GyroSensorUseCaseImpl @Inject constructor(
    private val repository: SensorRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GyroSensorUseCase {

    override fun getGyroFlow(): Flow<SensorEvent?> {
        return repository.getGyroFlow().flowOn(ioDispatcher)
    }
}