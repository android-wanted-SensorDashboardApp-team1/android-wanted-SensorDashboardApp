package com.preonboarding.sensordashboard.data.repository

import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : SensorRepository {

    override fun getAccFlow(): Flow<SensorAxisData> {
        return localDataSource.getAccFlow()
    }

    override fun getGyroFlow(): Flow<SensorAxisData> {
        return localDataSource.getGyroFlow()
    }

    override fun errorFlow(): MutableSharedFlow<Throwable> {
        return localDataSource.getErrorFlow()
    }

    override suspend fun insertSensorData(sensorData: SensorData) {
        localDataSource.insertSensorData(sensorData)
    }

    override fun getSensorDataFlow(): Flow<List<SensorData?>> {
        return localDataSource.getSensorDataFlow()
    }

    override suspend fun deleteSensorData(id: Long) {
        localDataSource.deleteSensorData(id)
    }
}