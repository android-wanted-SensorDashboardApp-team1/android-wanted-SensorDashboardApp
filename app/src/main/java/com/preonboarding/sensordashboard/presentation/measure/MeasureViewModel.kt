package com.preonboarding.sensordashboard.presentation.measure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.model.SensorType
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Created by 김현국 2022/09/29
 */

@HiltViewModel
class MeasureViewModel @Inject constructor(
    private val accSensorUseCase: AccSensorUseCase,
    private val gyroSensorUseCase: GyroSensorUseCase,
    private val roomUseCase: RoomUseCase
) : ViewModel() {

    private val _measuredSensorData = MutableStateFlow(
        SensorData(
            0f,
            0f,
            0f,
            SensorType.EMPTY,
            ""
        )
    )
    val measuredSensorData = _measuredSensorData.asStateFlow()

    private val sensorDataList = mutableListOf<SensorData>()

    fun measureGyroSensor() {
        viewModelScope.launch {
            gyroSensorUseCase.getGyroFlow().collect { sensorData ->
                _measuredSensorData.value = sensorData
            }
        }
    }

    fun measureAccSensor() {
        viewModelScope.launch {
            accSensorUseCase.getAccFlow().collect { sensorData ->
                _measuredSensorData.value = sensorData
            }
        }
    }

    fun addSensorData(sensorData: SensorData) {
        sensorDataList.add(sensorData)
    }

    fun saveSensorData() {
        viewModelScope.launch {
            for (sensorData in sensorDataList) {
                roomUseCase.insertSensorData(sensorData = sensorData)
            }
        }
    }
}
