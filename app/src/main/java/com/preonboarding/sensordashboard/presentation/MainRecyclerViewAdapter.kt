package com.preonboarding.sensordashboard.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.sensordashboard.databinding.ItemMainRecyclerviewBinding
import com.preonboarding.sensordashboard.domain.model.SensorData

class MainRecyclerViewAdapter(val viewModel: SensorViewModel) :
    PagingDataAdapter<SensorData, MainRecyclerViewAdapter.ViewHolder>(SensorDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMainRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(private val binding: ItemMainRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sensorData: SensorData) {
            binding.viewModel = viewModel
            binding.sensor = sensorData
            binding.executePendingBindings()
        }
    }
}

class SensorDiffCallback : DiffUtil.ItemCallback<SensorData>() {
    override fun areItemsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
        return oldItem == newItem
    }
}