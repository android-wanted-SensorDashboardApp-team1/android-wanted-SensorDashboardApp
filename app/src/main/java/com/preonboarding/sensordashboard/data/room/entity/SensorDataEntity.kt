package com.preonboarding.sensordashboard.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.preonboarding.sensordashboard.domain.model.SensorType

@Entity(tableName = "SensorDataEntity")
data class SensorDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "dataList") val dataList: String,
    @ColumnInfo(name = "type") val type: SensorType,
    @ColumnInfo(name = "date") val date: String
) {

    companion object {
        val EMPTY = SensorDataEntity(
            id = 0,
            dataList = "",
            type = SensorType.EMPTY,
            date = ""
        )
    }
}
