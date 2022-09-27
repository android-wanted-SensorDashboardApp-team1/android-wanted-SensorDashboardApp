package com.preonboarding.sensordashboard.domain.usecase

import android.hardware.TriggerEvent
import kotlinx.coroutines.flow.Flow

interface AccSensorUseCase {

    fun getAccFlow():Flow<TriggerEvent?>
}