package com.preonboarding.sensordashboard.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.preonboarding.sensordashboard.domain.model.SensorType

@Entity(tableName = "SensorDataEntity")
data class SensorDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "axisX") val x: Float,
    @ColumnInfo(name = "axisY") val y: Float,
    @ColumnInfo(name = "axisZ") val z: Float,
    @ColumnInfo(name = "type") val type: SensorType,
    @ColumnInfo(name = "date") val date: String
) {

    companion object {
        val EMPTY = SensorDataEntity(
            id = -1,
            x = 0f,
            y = 0f,
            z = 0f,
            type = SensorType.EMPTY,
            date = ""
        )
    }

}