package com.preonboarding.sensordashboard.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.data.dto.AxisData
import com.preonboarding.sensordashboard.databinding.SensorItemBinding
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData

class SensorPagingAdapter : PagingDataAdapter<SensorData, SensorPagingAdapter.SensorViewHolder>(
    object : DiffUtil.ItemCallback<SensorData>() {
        override fun areItemsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SensorData, newItem: SensorData): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item.dataList[0])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val binding = DataBindingUtil.inflate<SensorItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.sensor_item,
            parent,
            false
        )

        return SensorViewHolder(binding)
    }

    inner class SensorViewHolder(private val binding: SensorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SensorAxisData) {
            binding.axisData = item
        }
    }
}