package com.preonboarding.sensordashboard.domain.repository

import com.preonboarding.sensordashboard.domain.model.SensorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface SensorRepository {

    fun getAccFlow(): Flow<SensorData>

    fun getGyroFlow(): Flow<SensorData>

    fun errorFlow(): MutableSharedFlow<Throwable>

    suspend fun insertSensorData(sensorData: SensorData)

    fun getSensorDataFlow(): Flow<List<SensorData>>
}