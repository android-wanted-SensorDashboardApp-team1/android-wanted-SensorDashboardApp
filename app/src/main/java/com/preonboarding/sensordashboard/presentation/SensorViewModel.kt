package com.preonboarding.sensordashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.ErrorUseCase
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val accSensorUseCase: AccSensorUseCase,
    private val gyroSensorUseCase: GyroSensorUseCase,
    private val errorUseCase: ErrorUseCase
) : ViewModel() {

    val accSensorFlow = accSensorUseCase.getAccFlow()

    private val errorFlow = errorUseCase.getErrorFlow()

    init {
        viewModelScope.launch {
            accSensorFlow
                .catch { errorFlow.emit(it) }
                .onEach { event -> Log.e("event", event?.values.contentToString()) }
                .collect()
        }

        viewModelScope.launch {
            errorFlow.collect {
                Log.e("error",it.stackTraceToString())
            }
        }
    }
}