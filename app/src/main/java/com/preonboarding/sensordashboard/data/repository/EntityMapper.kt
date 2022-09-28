package com.preonboarding.sensordashboard.data.repository

import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity
import com.preonboarding.sensordashboard.domain.model.SensorData

fun SensorData.toEntity() = SensorDataEntity.EMPTY.copy(
    x= this.x,
    y= this.y,
    z= this.z,
    type = this.type,
    date = this.date
)

fun SensorDataEntity.toModel() = SensorData(
    x = this.x,
    y = this.y,
    z = this.z,
    type = this.type,
    date = this.date
)