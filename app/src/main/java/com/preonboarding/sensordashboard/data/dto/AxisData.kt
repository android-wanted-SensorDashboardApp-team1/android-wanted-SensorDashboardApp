package com.preonboarding.sensordashboard.data.dto

import com.preonboarding.sensordashboard.domain.model.SensorType

data class AxisData(
    val x: Float,
    val y: Float,
    val z: Float,
    val type: SensorType
)