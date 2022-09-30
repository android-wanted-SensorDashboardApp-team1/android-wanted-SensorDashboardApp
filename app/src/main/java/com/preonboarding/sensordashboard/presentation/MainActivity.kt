package com.preonboarding.sensordashboard.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.ActivityMainBinding
import com.preonboarding.sensordashboard.domain.model.SensorData
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

        val swipeController = SwipeController(recyclerViewAdapter)
        ItemTouchHelper(swipeController).attachToRecyclerView(binding.recyclerviewMain)

        recyclerViewAdapter.setOnSwipeClickListener(object :
            MainRecyclerViewAdapter.OnSwipeClickListener {
            override fun onRightClick(sensorData: SensorData) {
                //다시보기 페이지로 이동시켜야함. id 넘겨줘야할듯. 물어보자
            }

            override fun onLeftClick(sensorData: SensorData) {
                sensorViewModel.deleteSensorData(sensorData.id)
            }
        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sensorViewModel.sensorsFlow.collectLatest { sensors ->
                    (binding.recyclerviewMain.adapter as MainRecyclerViewAdapter).submitList(sensors)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.item_main_measure -> {
                //화면전환 코드 추가 필요
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}