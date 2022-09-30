package com.preonboarding.sensordashboard.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.sensordashboard.databinding.ItemMainRecyclerviewBinding
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.presentation.SensorViewModel

class MainRecyclerViewAdapter(val viewModel: SensorViewModel) :

    PagingDataAdapter<SensorData, MainRecyclerViewAdapter.ViewHolder>(SensorDiffCallback()),
    ItemTouchHelperListener {

    private lateinit var onSwipeClickListener: OnSwipeClickListener

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

    fun setOnSwipeClickListener(listener: OnSwipeClickListener) {
        onSwipeClickListener = listener
    }

    override fun onLeftView(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        onSwipeClickListener.onLeftView(getItem(position) ?: SensorData.EMPTY)
    }

    override fun onRightView(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        onSwipeClickListener.onRightView(getItem(position) ?: SensorData.EMPTY)
        notifyItemChanged(position)
    }

    interface OnSwipeClickListener {
        fun onLeftView(sensorData: SensorData)
        fun onRightView(sensorData: SensorData)
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