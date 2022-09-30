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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
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

    private val errorFlow = errorUseCase.getErrorFlow()

    private val accSensorDataList = mutableListOf<SensorAxisData>()

    val sensorDataPagingFlow = roomUseCase.getSensorPagingDataFlow()

    init {
        viewModelScope.launch {
            roomUseCase.addSensorDataTestData()
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
                    type = SensorType.ACC
                )
            )

            accSensorDataList.clear()
        }
    }
}