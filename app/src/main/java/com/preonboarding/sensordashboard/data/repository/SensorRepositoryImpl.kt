package com.preonboarding.sensordashboard.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val json: Json
) : SensorRepository {

    override fun SensorDataPagingSource(): Flow<PagingData<SensorData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            initialKey = 1,
            pagingSourceFactory = { localDataSource.getSensorDataPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toModel(json)
            }
        }
    }

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

    override suspend fun addSensorTestData() {
        localDataSource.addTestSensorData()
    }
}