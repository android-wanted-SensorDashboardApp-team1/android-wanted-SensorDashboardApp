package com.preonboarding.sensordashboard.domain.repository

import android.hardware.SensorEvent
import kotlinx.coroutines.flow.Flow

interface SensorRepository {

    fun getAccFlow(): Flow<SensorEvent?>

    fun getGyroFlow(): Flow<SensorEvent?>
}