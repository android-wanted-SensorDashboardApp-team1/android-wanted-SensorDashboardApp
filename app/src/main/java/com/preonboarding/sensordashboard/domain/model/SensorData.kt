package com.preonboarding.sensordashboard.domain.model

data class SensorData(
    val x: Double,
    val y: Double,
    val z: Double,
    val type: SensorType
)
