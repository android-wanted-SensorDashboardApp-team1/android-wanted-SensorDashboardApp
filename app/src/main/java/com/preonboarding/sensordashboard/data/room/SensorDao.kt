package com.preonboarding.sensordashboard.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.sensordashboard.data.room.entity.SensorAxisDataEntity
import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSensorData(sensorDataEntity: SensorDataEntity)

    @Query("SELECT * FROM SensorDataEntity ORDER BY dateValue DESC")
    fun getSensorDataFlow(): Flow<List<SensorDataEntity>>

}