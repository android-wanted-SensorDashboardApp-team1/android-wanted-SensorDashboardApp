package com.preonboarding.sensordashboard.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorAxisData(
    val x: Float,
    val y: Float,
    val z: Float
)
