package com.preonboarding.sensordashboard.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val sensorViewModel: SensorViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private val recyclerViewAdapter = MainRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = sensorViewModel
        binding.lifecycleOwner = this
        binding.recyclerviewMain.apply {
            adapter = recyclerViewAdapter
        }
        lifecycleScope.launch {
            sensorViewModel.sensorsFlow.collectLatest { sensors ->
                (binding.recyclerviewMain.adapter as MainRecyclerViewAdapter).submitList(sensors)
            }
        }
    }
}