package com.preonboarding.sensordashboard.domain.usecase

import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import kotlinx.coroutines.flow.Flow

interface GyroSensorUseCase {

    fun getGyroFlow(): Flow<SensorAxisData>
}