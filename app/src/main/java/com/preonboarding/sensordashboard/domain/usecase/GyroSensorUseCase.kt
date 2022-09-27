package com.preonboarding.sensordashboard.domain.usecase

import android.hardware.TriggerEvent
import kotlinx.coroutines.flow.Flow

interface GyroSensorUseCase {

    fun getGyroFlow(): Flow<TriggerEvent?>
}