package com.preonboarding.sensordashboard.presentation.measure

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.ActivityMeasureBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MeasureActivity : AppCompatActivity() {
    private val viewModel: MeasureViewModel by viewModels()
    private val binding by lazy { ActivityMeasureBinding.inflate(layoutInflater) }
    private val sensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val gyroScope by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) }
    private val accelerometer by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    private val timer by lazy { Timer() }
    private val TAG = "MeasureActivity"
    var gsensorCount = 0
    var ssensorCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
        initChart()
        observeSensor()
    }

    private fun initListener() = with(binding) {
        var type = "Acc"
        radioGroup.check(R.id.radio_button_acc)
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.radio_button_acc -> {
                    type = "Acc"
                    initChart()
                }
                R.id.radio_button_gyro -> {
                    type = "Gyro"
                    initChart()
                }
            }
        }
        tvMeasure.setOnClickListener {
            var count = 1
            radioGroup.get(0).isEnabled = false
            radioGroup.get(1).isEnabled = false
//            timer.schedule(
//                object : TimerTask() {
//                    override fun run() {
//                        count++
//                        if (count == 60) {
//                            timer.cancel()
// //                            sensorManager.unregisterListener(this@MeasureActivity)
//                            radioGroup.get(0).isEnabled = true
//                            radioGroup.get(1).isEnabled = true
//                        }
//                    }
//                },
//                1000,
//                1000
//            )
            tvStop.visibility = 0
            when (type) {
                "Acc" -> {
                    viewModel.measureAccSensor()
                }
                "Gyro" -> {
                    viewModel.measureGyroSensor()
                }
            }
        }
        tvStop.setOnClickListener {
//            sensorManager.unregisterListener(this@MeasureActivity)
            radioGroup.get(0).isEnabled = true
            radioGroup.get(1).isEnabled = true
            tvStop.visibility = 4
        }
    }

    private fun observeSensor() = with(lifecycleScope) {
        launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                with(viewModel) {
                    measuredSensorData.collect { sensorData ->
                        addSensorData(sensorData)
                        addEntry(sensorData.x.toDouble(), label = "x")
                        addEntry(sensorData.y.toDouble(), label = "y")
                        addEntry(sensorData.z.toDouble(), label = "z")
                    }
                }
            }
        }
    }

    private fun initChart() = with(binding.lineChart) {
        setDrawGridBackground(true)
        setBackgroundColor(Color.WHITE)
        setGridBackgroundColor(Color.WHITE)
        description.text = ""

        setTouchEnabled(false)
        setScaleEnabled(false)

        isAutoScaleMinMaxEnabled = true
        setPinchZoom(false)

        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(false)

        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)

        // Y
        axisLeft.isEnabled = true
        axisLeft.setDrawGridLines(true)
        axisRight.isEnabled = false

        val entryX = mutableListOf<Entry>()
        val entryY = mutableListOf<Entry>()
        val entryZ = mutableListOf<Entry>()

        val xCharDataSet = createSet(color = Color.RED, label = "x", entryX)
        val yChartDataSet = createSet(color = Color.GREEN, label = "y", entryY)
        val zChartDataSet = createSet(color = Color.BLUE, label = "z", entryZ)

        val dataSets = arrayListOf<ILineDataSet>()
        dataSets.add(xCharDataSet)
        dataSets.add(yChartDataSet)
        dataSets.add(zChartDataSet)
        val chartData = LineData(dataSets)

        val legend = legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.textSize = 15f
        legend.xEntrySpace = 15f
        val legendEntryX = LegendEntry()
        legendEntryX.formColor = Color.RED
        legendEntryX.label = "x"
        val legendEntryY = LegendEntry()
        legendEntryY.formColor = Color.GREEN
        legendEntryY.label = "y"

        val legendEntryZ = LegendEntry()
        legendEntryZ.formColor = Color.BLUE
        legendEntryZ.label = "z"
        val legendList = listOf(legendEntryX, legendEntryY, legendEntryZ)
        legend.setCustom(legendList)

        data = chartData

        invalidate()
    }

    private fun createSet(color: Int, label: String, entry: MutableList<Entry>): LineDataSet {
        val set = LineDataSet(entry, label)
        set.lineWidth = 1f
        set.setDrawValues(false)
        set.valueTextColor = Color.WHITE
        set.color = color
        set.mode = LineDataSet.Mode.LINEAR
        set.setDrawCircles(false)

        return set
    }

    private fun addEntry(num: Double, label: String) = with(binding.lineChart) {
        when (label) {
            "x" -> {
                val set = data.getDataSetByLabel("x", false)
                data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 0)
            }
            "y" -> {
                val set = data.getDataSetByLabel("y", false)
                data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 1)
            }
            "z" -> {
                val set = data.getDataSetByLabel("z", false)
                data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 2)
            }
        }
        data.notifyDataChanged()

        notifyDataSetChanged()
        moveViewTo(data.entryCount.toFloat(), 50f, YAxis.AxisDependency.LEFT)
    }

//    override fun onSensorChanged(sensorEvent: SensorEvent) {
//        val sensor = sensorEvent.sensor
//
//        if (sensor.type == Sensor.TYPE_GYROSCOPE) {
//            val gyroX = (sensorEvent.values[0] * 1000)
//            val gyroY = (sensorEvent.values[1] * 1000)
//            val gyroZ = (sensorEvent.values[2] * 1000)
//
//            addEntry(gyroX.toDouble(), label = "x")
//            addEntry(gyroY.toDouble(), label = "y")
//            addEntry(gyroZ.toDouble(), label = "z")
//
//            gsensorCount++
//        }
//        if (sensor.type == Sensor.TYPE_ACCELEROMETER) {
//            val acceleX = sensorEvent.values[0]
//            val acceleY = sensorEvent.values[1]
//            val acceleZ = sensorEvent.values[2]
//
//            addEntry(acceleX.toDouble(), label = "x")
//            addEntry(acceleY.toDouble(), label = "y")
//            addEntry(acceleZ.toDouble(), label = "z")
//            ssensorCount++
//        }
//    }
//
//    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//    }

    override fun onPause() {
        super.onPause()
//        sensorManager.unregisterListener(this)
    }
}
