package com.preonboarding.sensordashboard.domain.model

data class SensorData(
    val x: Float,
    val y: Float,
    val z: Float,
    val type: SensorType,
    val date: String
)
