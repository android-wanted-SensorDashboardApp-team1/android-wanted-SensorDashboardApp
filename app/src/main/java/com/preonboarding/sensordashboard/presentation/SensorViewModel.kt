package com.preonboarding.sensordashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.ErrorUseCase
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    init {
        viewModelScope.launch {
            gyroSensorFlow
                .catch { errorFlow.emit(it) }
                .onEach { event -> Log.e("event", event.toString()) }
                .collect()
        }

        viewModelScope.launch {
            errorFlow.collect {
                Log.e("error", it.stackTraceToString())
            }
        }
    }
}