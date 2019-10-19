package com.matmik.mapalarm.android.model

import java.util.*

data class Alarm(
    var id: Int = -1,
    var name: String,
    var time: Date,
    var options: List<Options>,
    var active:Boolean = true,
    var locationBound: Boolean = false,
    var location: String = "")