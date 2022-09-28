package com.preonboarding.sensordashboard.domain.usecase

import kotlinx.coroutines.flow.MutableSharedFlow

interface ErrorUseCase {

    fun getErrorFlow(): MutableSharedFlow<Throwable>
}