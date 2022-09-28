package com.preonboarding.sensordashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.model.SensorType
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.ErrorUseCase
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val accSensorUseCase: AccSensorUseCase,
    private val gyroSensorUseCase: GyroSensorUseCase,
    private val errorUseCase: ErrorUseCase,
    private val roomUseCase: RoomUseCase
) : ViewModel() {

    val accSensorFlow = accSensorUseCase.getAccFlow()
    val gyroSensorFlow = gyroSensorUseCase.getGyroFlow()

    private val errorFlow = errorUseCase.getErrorFlow()

    private val accSensorDataList = mutableListOf<SensorAxisData>()

    init {
//        viewModelScope.launch { //Sensor data 수집
//            accSensorFlow
//                .onEach { accSensorDataList.add(it) }
////                .onEach { Timber.e(it.toString()) }
//                .collect()
//        }

        viewModelScope.launch {
            roomUseCase.getSensorDataFlow()
                .onEach { Timber.e(it.toString()) }
                .collect()
        }

        viewModelScope.launch {
            errorFlow.collect {
                Timber.e(it.stackTraceToString())
            }
        }
    }

    fun testInsert() {
        viewModelScope.launch {
            roomUseCase.insertSensorData(
                SensorData.EMPTY.copy(
                    dataList = accSensorDataList,
                    type = SensorType.GYRO
                )
            )

            accSensorDataList.clear()
        }
    }
}