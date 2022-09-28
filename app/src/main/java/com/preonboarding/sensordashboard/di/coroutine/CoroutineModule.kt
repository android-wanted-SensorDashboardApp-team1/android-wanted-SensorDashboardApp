package com.preonboarding.sensordashboard.di.coroutine

import com.preonboarding.sensordashboard.di.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @SensorScopeQualifier
    @Provides
    @Singleton
    fun provideSensorScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + newSingleThreadContext("SensorErrorThread"))
    }

}