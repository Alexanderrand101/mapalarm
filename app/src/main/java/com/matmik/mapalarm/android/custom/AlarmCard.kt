package com.matmik.mapalarm.android.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.matmik.mapalarm.android.R
import com.matmik.mapalarm.android.model.Alarm

class AlarmCard(var alarm:Alarm, context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    init{
        View.inflate(context, R.layout.alarm_card_content, this)
        val alarmNameBox = findViewById<TextView>(R.id.alarm_name)
        val alarmTimeBox = findViewById<TextView>(R.id.alarm_time)
        val alarmIsActiveSwitch = findViewById<Switch>(R.id.alarm_active_btn)
        alarmNameBox.text = alarm.name;
        alarmTimeBox.text = alarm.time.hours.toString() + ":" + alarm.time.minutes.toString()
    }
}