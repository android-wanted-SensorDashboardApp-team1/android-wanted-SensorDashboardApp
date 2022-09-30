package com.preonboarding.sensordashboard.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.sensordashboard.databinding.ItemMainRecyclerviewBinding
import com.preonboarding.sensordashboard.domain.model.SensorData

class MainRecyclerViewAdapter(val viewModel: SensorViewModel) :
    ListAdapter<SensorData, MainRecyclerViewAdapter.ViewHolder>(SensorDiffCallback()),
    ItemTouchHelperListener {

    private lateinit var onSwipeClickListener: OnSwipeClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMainRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        return false
    }

    override fun onItemSwipe(position: Int) {

    }

    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        onSwipeClickListener.onLeftClick(getItem(position))
    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        onSwipeClickListener.onRightClick(getItem(position))
    }

    interface OnSwipeClickListener {
        fun onLeftClick(sensorData: SensorData)
        fun onRightClick(sensorData: SensorData)
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