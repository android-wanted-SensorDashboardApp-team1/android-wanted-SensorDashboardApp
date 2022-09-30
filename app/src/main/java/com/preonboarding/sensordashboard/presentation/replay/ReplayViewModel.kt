package com.preonboarding.sensordashboard.presentation.replay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Created by 김현국 2022/09/30
 */
@HiltViewModel
class ReplayViewModel @Inject constructor(
    private val roomUseCase: RoomUseCase
) : ViewModel() {

    private val _sensorData: MutableStateFlow<SensorData> = MutableStateFlow(SensorData.EMPTY)
    val sensorData = _sensorData.asStateFlow()

    private val _sensorUnitData: MutableStateFlow<SensorAxisData> = MutableStateFlow(SensorAxisData(0f, 0f, 0f))
    private val _sensorBindingData: MutableSharedFlow<SensorAxisData> = MutableSharedFlow<SensorAxisData>(replay = 0, 1, BufferOverflow.DROP_OLDEST)
    val sensorBindingData = _sensorBindingData.asSharedFlow()

    private val _stopPressed: MutableSharedFlow<Unit> = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)
    val stopPressed = _stopPressed.asSharedFlow()

    lateinit var updateJob: Job

    private val _currentReplayTime: MutableStateFlow<Float> = MutableStateFlow(0f)
    val currentReplayTime = _currentReplayTime.asSharedFlow()

    init {
        getDataUnit()
    }
    fun getDataUnit() {
        viewModelScope.launch {
            roomUseCase.getSensorDataFlow().collect { list ->
                if (list.isNotEmpty() && list.last() != null) {
                    _sensorData.value = sensorData.value.copy(
                        dataList = list.last()!!.dataList,
                        date = list.last()!!.date,
                        time = list.last()!!.time
                    )
                }
            }
        }
    }

    private fun emitUnitData() {
        val job = viewModelScope.launch {
            _sensorUnitData.collect { sensorAxisData ->
                _sensorBindingData.tryEmit(sensorAxisData)
            }
        }
        viewModelScope.launch {
            _stopPressed.collect {
                job.cancelAndJoin()
            }
        }
    }

    fun pressStopButton() {
        viewModelScope.launch {
            _stopPressed.tryEmit(Unit)
            updateJob.cancelAndJoin()
        }
    }
    fun updateSensorUnitData(sensorAxisDataList: List<SensorAxisData>) {
        emitUnitData()
        initCurrentReplayTime()
        println(sensorAxisDataList.size)
        updateJob = viewModelScope.launch {
            sensorAxisDataList.forEachIndexed { index, sensorAxisData ->
                _sensorUnitData.value = sensorAxisData
                delay(100)
                _currentReplayTime.value = (index + 1) / 10f
                if (index == sensorAxisDataList.size - 1) {
                    _stopPressed.tryEmit(Unit)
                }
            }
        }
    }

    private fun initCurrentReplayTime() {
        _currentReplayTime.value = 0f
    }
}
