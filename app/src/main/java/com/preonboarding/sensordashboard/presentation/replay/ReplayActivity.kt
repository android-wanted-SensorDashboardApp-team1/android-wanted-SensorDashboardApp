package com.preonboarding.sensordashboard.presentation.replay

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.ActivityReplayBinding
import com.preonboarding.sensordashboard.domain.model.SensorData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class ReplayActivity : AppCompatActivity() {

    private val binding by lazy { ActivityReplayBinding.inflate(layoutInflater) }
    private val viewModel: ReplayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeData()
        initChart()
        initListener()
    }

    private fun initListener() = with(binding) {
        ivPlay.setOnClickListener {
            initChart()
            viewModel.getDataUnit()
            viewModel.initCurrentReplayTime()
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.sensorData.collect {
                        if (it.dataList.isNotEmpty()) {
                            initUI(it)
                        }
                    }
                }
            }
        }
        ivStop.setOnClickListener {
            viewModel.pressStopButton()
            ivPlay.visibility = View.VISIBLE
            ivStop.visibility = View.GONE
            tvCurrentState.text = getString(R.string.replay_state_stop)
        }
        ivBack.setOnClickListener {
            finish()
        }
    }

    private fun observeData() = with(lifecycleScope) {
        launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                viewModel.sensorBindingData.collect {
                    addEntry(it.x.toDouble(), "x")
                    addEntry(it.y.toDouble(), "y")
                    addEntry(it.z.toDouble(), "z")
                    binding.tvX.text = it.x.toString()
                    binding.tvY.text = it.y.toString()
                    binding.tvZ.text = it.z.toString()
                }
            }
        }
        launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                viewModel.stopPressed.collect {
                    with(binding) {
                        ivPlay.visibility = View.VISIBLE
                        ivStop.visibility = View.GONE
                    }
                }
            }
        }
        launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                viewModel.currentReplayTime.collect { time ->
                    val updateTime = DecimalFormat("#.#").format(time)
                    binding.tvTime.text = updateTime
                }
            }
        }
    }

    private fun initUI(sensorData: SensorData) = with(binding) {
        tvDate.text = sensorData.date
        tvCurrentState.text = getString(R.string.replay_state_play)
        ivPlay.visibility = View.GONE
        ivStop.visibility = View.VISIBLE
        viewModel.updateSensorUnitData(sensorData.dataList)
    }

    private fun initChart() = with(binding.lineChart) {
        setDrawGridBackground(true)
        setBackgroundColor(Color.WHITE)
        setGridBackgroundColor(Color.WHITE)
        description.text = ""

        setTouchEnabled(true)
        setScaleEnabled(false)

        isAutoScaleMinMaxEnabled = true
        setPinchZoom(false)

        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(false)
        xAxis.isEnabled = true

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
                val set = data.getDataSetByLabel(label, false)
                data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 0)
            }
            "y" -> {
                val set = data.getDataSetByLabel(label, false)
                data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 1)
            }
            "z" -> {
                val set = data.getDataSetByLabel(label, false)
                data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 2)
            }
        }
        data.notifyDataChanged()
        notifyDataSetChanged()
        setVisibleXRangeMaximum(10f)
        moveViewToX(data.entryCount.toFloat())
    }
}
