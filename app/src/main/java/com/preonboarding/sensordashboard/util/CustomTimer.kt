package com.preonboarding.sensordashboard.util

import android.annotation.SuppressLint
import android.util.Log
import com.preonboarding.sensordashboard.di.IoDispatcher
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

object CustomTimer {

    /** TODO        Custom Timer 사용법
     *  TODO        타이머 Start -> 1. 60초 타이머  :  CustomTimer.setTimerState(TimerState.Start)
     *  TODO                    -> 2. 커스텀 타이머 ( ex 30초 ): CustomTimer.setTimerState(TimerState.Start, "00:30:00")
     *  TODO        타이머 Stop  ->  CustomTimer.setTimerState(TimerState.Stop)
     */

    private var timerJob : Job = Job()

    @IoDispatcher
    @Inject
    lateinit var ioDispatcher : CoroutineDispatcher
    private val coroutineScope = CoroutineScope(ioDispatcher)
    private var isActive = false
    private const val MAX_TIME = 60000L
    private var timerCount = 0L

    var formatTime = ""

    @SuppressLint("SimpleDateFormat")
    fun setTimerState(state: CustomTimerState, lastResumedTime : String? = null ) {
        val startDate = lastResumedTime?.let { SimpleDateFormat("HH:mm:ss").parse(it) }

        if ( lastResumedTime != null ) {
            if (startDate != null)  this.timerCount = startDate.time.toLong()
        } else {
            this.timerCount = MAX_TIME
        }

        when (state) {
            is CustomTimerState.Start ->  startTimerJob()
            is CustomTimerState.Stop -> stopTimerJob()
        }
    }

    private fun startTimerJob(){
        if (timerJob.isActive) timerJob.cancel()
        val form = SimpleDateFormat(" HH:mm:ss")

        timerJob = coroutineScope.launch {
            withContext(ioDispatcher) {
                this@CustomTimer.isActive = true
                while ( timerCount >= 0 && timerJob.isActive ) {
                    delay(1000L)
                    timerCount -= 1000L
                    formatTime = form.format(Date(timerCount))
                }
                this@CustomTimer.isActive = false
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun stopTimerJob():String{
        isActive = false
        if (timerJob.isActive) timerJob.cancel()
        return SimpleDateFormat(" HH:mm:ss").format(Date(timerCount))
    }

}