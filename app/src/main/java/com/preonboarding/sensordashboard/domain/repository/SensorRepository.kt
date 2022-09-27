package com.preonboarding.sensordashboard.domain.repository

import android.hardware.TriggerEvent
import kotlinx.coroutines.flow.Flow

interface SensorRepository {

    fun getAccFlow(): Flow<TriggerEvent?>

    fun getGyroFlow(): Flow<TriggerEvent?>
}