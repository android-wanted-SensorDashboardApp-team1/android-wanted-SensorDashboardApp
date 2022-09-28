package com.preonboarding.sensordashboard.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity
import com.preonboarding.sensordashboard.domain.model.SensorData
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSensorData(sensorDataEntity: SensorDataEntity)

    @Query("SELECT * FROM SensorDataEntity")
    fun getSensorDataFlow(): Flow<SensorDataEntity>

}