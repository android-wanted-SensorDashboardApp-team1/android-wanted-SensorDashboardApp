package com.preonboarding.sensordashboard.presentation

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {
    fun onLeftView(position: Int, viewHolder: RecyclerView.ViewHolder?)
    fun onRightView(position: Int, viewHolder: RecyclerView.ViewHolder?)
}