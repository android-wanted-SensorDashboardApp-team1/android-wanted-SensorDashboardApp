package com.preonboarding.sensordashboard.domain.model

import com.preonboarding.sensordashboard.util.DateUtil

data class SensorData(
    val dataList: List<SensorAxisData>,
    val type: SensorType,
    val date: String
) {

    companion object {
        val EMPTY = SensorData(
            dataList = mutableListOf(),
            type = SensorType.EMPTY,
            date = DateUtil.getCurrentTime()
        )
    }
}
