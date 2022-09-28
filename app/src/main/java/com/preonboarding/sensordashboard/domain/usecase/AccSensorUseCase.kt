package com.preonboarding.sensordashboard.domain.usecase

import com.preonboarding.sensordashboard.domain.model.SensorData
import kotlinx.coroutines.flow.Flow

interface AccSensorUseCase {

    fun getAccFlow():Flow<SensorData>
}