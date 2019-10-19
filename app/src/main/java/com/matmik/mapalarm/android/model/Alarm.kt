package com.matmik.mapalarm.android.model

import java.io.Serializable
import java.util.*


data class Alarm (
    var id: Int = -1,
    var name: String,
    var time: Date,
    var options: MutableList<Options>,
    var active:Boolean = true,
    var locationBound: Boolean = false,
    var location: String = ""): Serializable