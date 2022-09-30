package com.preonboarding.sensordashboard.domain.repository

import androidx.paging.PagingData
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface SensorRepository {

    fun SensorDataPagingSource(): Flow<PagingData<SensorData>>

    fun getAccFlow(): Flow<SensorAxisData>

    fun getGyroFlow(): Flow<SensorAxisData>

    fun errorFlow(): MutableSharedFlow<Throwable>

    suspend fun insertSensorData(sensorData: SensorData)

    fun getSensorDataFlow(): Flow<List<SensorData?>>

    suspend fun addSensorTestData()
}