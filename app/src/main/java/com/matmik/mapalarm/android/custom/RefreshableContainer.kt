package com.matmik.mapalarm.android.custom

import com.matmik.mapalarm.android.model.Alarm
import org.osmdroid.util.GeoPoint

interface RefreshableContainer {
    fun refreshTb(alarm: Alarm?)
    fun snapToLoc(point:GeoPoint)
}