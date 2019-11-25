package com.matmik.mapalarm.android

import org.osmdroid.views.overlay.OverlayItem

fun Boolean.toInt(): Int = if (this) 1 else 0

object GlobalVariables {
    val items: MutableList<OverlayItem> = ArrayList()
}