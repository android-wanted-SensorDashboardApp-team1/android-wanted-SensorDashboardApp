package com.preonboarding.sensordashboard.domain.usecase

import android.hardware.SensorEvent
import kotlinx.coroutines.flow.Flow

interface GyroSensorUseCase {

    fun getGyroFlow(): Flow<SensorEvent?>
}