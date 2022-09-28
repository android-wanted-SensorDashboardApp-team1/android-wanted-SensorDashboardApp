package com.preonboarding.sensordashboard.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.preonboarding.sensordashboard.data.room.SensorDataBase.Companion.ROOM_VERSION
import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity

@Database(entities = [SensorDataEntity::class], version = ROOM_VERSION, exportSchema = false)
abstract class SensorDataBase :RoomDatabase() {

    abstract fun sensorDao(): SensorDao


    companion object {
        const val ROOM_VERSION = 1
    }
}