package com.preonboarding.sensordashboard.domain.usecase

import android.hardware.SensorEvent
import kotlinx.coroutines.flow.Flow

interface AccSensorUseCase {

    fun getAccFlow():Flow<SensorEvent?>
}