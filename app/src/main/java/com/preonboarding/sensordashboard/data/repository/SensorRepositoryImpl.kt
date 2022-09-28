package com.preonboarding.sensordashboard.data.repository

import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : SensorRepository {

    override fun getAccFlow(): Flow<SensorData> {
        return localDataSource.getAccFlow()
    }

    override fun getGyroFlow(): Flow<SensorData> {
        return localDataSource.getGyroFlow()
    }

    override fun errorFlow(): MutableSharedFlow<Throwable> {
        return localDataSource.getErrorFlow()
    }

    override suspend fun insertSensorData(sensorData: SensorData) {
        localDataSource.insertSensorData(sensorData)
    }

    override fun getSensorDataFlow(): Flow<List<SensorData>> {
        return localDataSource.getSensorDataFlow()
    }
}