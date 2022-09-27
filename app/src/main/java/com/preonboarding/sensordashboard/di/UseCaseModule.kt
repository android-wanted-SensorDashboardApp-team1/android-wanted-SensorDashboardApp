package com.preonboarding.sensordashboard.di

import com.preonboarding.sensordashboard.data.usecase.AccSensorUseCaseImpl
import com.preonboarding.sensordashboard.data.usecase.GyroSensorUseCaseImpl
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsAccUseCase(gyroUseCaseImpl: AccSensorUseCaseImpl): AccSensorUseCase


    @Binds
    @ViewModelScoped
    abstract fun bindsGyroUseCase(gyroUseCaseImpl: GyroSensorUseCaseImpl): GyroSensorUseCase

}