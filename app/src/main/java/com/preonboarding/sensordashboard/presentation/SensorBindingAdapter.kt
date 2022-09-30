package com.preonboarding.sensordashboard.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter

object SensorBindingAdapter {


    @BindingAdapter("app:axisText")
    @JvmStatic
    fun setAxisToString(textView: TextView, axis: Float?) {
        textView.text = axis.toString()
    }

}