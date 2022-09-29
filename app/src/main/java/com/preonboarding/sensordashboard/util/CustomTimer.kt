package com.preonboarding.sensordashboard.util

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

object CustomTimer {

    private var timerJob : Job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

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
        Log.i("CustomTimer","타이머 시작  "+ timerJob.key)

        timerJob = coroutineScope.launch {
            withContext(Dispatchers.IO) {
                this@CustomTimer.isActive = true
                while (timerCount >= 0) {
                    delay(1000L)
                    timerCount -= 1000L
                    formatTime = SimpleDateFormat(" HH:mm:ss").format(Date(timerCount))
                    Log.i("CustomTimer 경과시간 ", formatTime + "@@@@@@@@@" + timerJob.key)
                }
                this@CustomTimer.isActive = false
            }
        }


    }

    @SuppressLint("SimpleDateFormat")
    private fun stopTimerJob():String{
        isActive = false
        Log.i("CustomTimer ","타이머 정지  " + timerJob.key)
        if (timerJob.isActive) timerJob.cancel()
        return SimpleDateFormat(" HH:mm:ss").format(Date(timerCount))
    }

}