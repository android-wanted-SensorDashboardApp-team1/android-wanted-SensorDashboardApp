package com.preonboarding.sensordashboard.domain.repository

import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface SensorRepository {

    fun getAccFlow(): Flow<SensorAxisData>

    fun getGyroFlow(): Flow<SensorAxisData>

    fun errorFlow(): MutableSharedFlow<Throwable>

    suspend fun insertSensorData(sensorData: SensorData)

    fun getSensorDataFlow(): Flow<List<SensorData?>>
}