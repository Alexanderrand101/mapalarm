package com.matmik.mapalarm.android.custom

import com.matmik.mapalarm.android.model.Alarm

interface RefreshableContainer {
    fun refreshTb(alarm: Alarm?)
}