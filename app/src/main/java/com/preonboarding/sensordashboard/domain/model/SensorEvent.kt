package com.preonboarding.sensordashboard.domain.model

data class SensorEvent(
    val acc: AccData,
    val gyro: GyroData,
    val Date: String
)