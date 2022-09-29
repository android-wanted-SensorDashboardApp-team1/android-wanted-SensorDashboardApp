package com.preonboarding.sensordashboard.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val sensorViewModel: SensorViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewAdapter: MainRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = sensorViewModel
        binding.lifecycleOwner = this
        binding.recyclerviewMain.apply {
            recyclerViewAdapter = MainRecyclerViewAdapter(sensorViewModel)
            adapter = recyclerViewAdapter
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sensorViewModel.sensorsFlow.collectLatest { sensors ->
                    (binding.recyclerviewMain.adapter as MainRecyclerViewAdapter).submitList(sensors)
                }
            }
        }
    }
}