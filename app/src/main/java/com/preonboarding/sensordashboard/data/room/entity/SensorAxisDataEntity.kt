package com.preonboarding.sensordashboard.data.room.entity

import kotlinx.serialization.Serializable

@Serializable
data class SensorAxisDataEntity(
    val x: Float,
    val y: Float,
    val z: Float
) {

    companion object {
        val EMPTY = SensorAxisDataEntity(
            x = 0f,
            y = 0f,
            z = 0f
        )
    }
}