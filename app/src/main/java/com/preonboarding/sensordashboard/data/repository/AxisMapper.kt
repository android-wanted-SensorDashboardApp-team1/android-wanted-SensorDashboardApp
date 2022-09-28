package com.preonboarding.sensordashboard.data.repository

import com.preonboarding.sensordashboard.data.dto.AxisData
import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity
import com.preonboarding.sensordashboard.domain.model.SensorData

fun AxisData.toSensorData() = SensorData(
    x = this.x,
    y = this.y,
    z = this.z,
    type = this.type,
    date = ""
)