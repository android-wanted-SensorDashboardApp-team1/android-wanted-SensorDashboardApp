package com.preonboarding.sensordashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val accSensorUseCase: AccSensorUseCase,
    private val gyroSensorUseCase: GyroSensorUseCase
) : ViewModel() {

    val accSensorFlow = accSensorUseCase.getAccFlow()


    init {
        viewModelScope.launch {
            accSensorFlow
                .catch { }
                .onEach { event -> Log.e("event", event?.values.contentToString()) }
                .collect()
        }
    }

}