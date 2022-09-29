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
    val sensorsFlow = roomUseCase.getSensorDataFlow()

    private val errorFlow = errorUseCase.getErrorFlow()

    private val accSensorDataList = mutableListOf<SensorAxisData>()

    init {
// todo Sensor관련된 Flow를 취소할 수 있는 예시에요. 불필요 시, 제거 필수!!
//        val job = viewModelScope.launch { //Sensor data 수집
//            accSensorUseCase.getAccFlow()
//                .onEach { accSensorDataList.add(it) }
//                .collect()
//        }
//        viewModelScope.launch {
//            delay(60000) // timeOut
//            job.cancelAndJoin() // collect() 취소를 위해서 job cancel
//        }

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
                    type = SensorType.ACC
                )
            )
            accSensorDataList.clear()
        }
    }

    fun deleteSensorData(id: Long) {
        viewModelScope.launch {
            roomUseCase.deleteSensorData(id)
        }
    }
}