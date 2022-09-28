package com.preonboarding.sensordashboard.domain.repository

import android.hardware.SensorEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface SensorRepository {

    fun getAccFlow(): Flow<SensorEvent?>

    fun getGyroFlow(): Flow<SensorEvent?>

    fun errorFlow(): MutableSharedFlow<Throwable>
}