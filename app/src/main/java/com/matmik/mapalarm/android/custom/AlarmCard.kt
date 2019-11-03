package com.matmik.mapalarm.android.custom

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.matmik.mapalarm.android.EditNoteActivity
import com.matmik.mapalarm.android.R
import com.matmik.mapalarm.android.db.DbHelper
import com.matmik.mapalarm.android.model.Alarm
import kotlinx.android.synthetic.main.alarm_card_content.view.*

class AlarmCard(var alarm:Alarm, val refreshableContainer: RefreshableContainer,context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private lateinit var dbHelper:DbHelper

    //Спроси приведет ли это к утечке
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.delete_alarm_btn ->{
                dbHelper.deleteAlarm(alarm)
                refreshableContainer.refreshTb()
            }
            R.id.alarm_active_btn->{
                alarm.active = !alarm.active
                dbHelper.updateAlarm(alarm)
            }
            R.id.edit_alarm_btn->{
                val editIntent = Intent(v.context, EditNoteActivity::class.java)
                editIntent.putExtra("EditableNote",alarm)
                startActivity(v.context, editIntent, null)
            }
        }
    }

    init{
        View.inflate(context, R.layout.alarm_card_content, this)
        val alarmNameBox = findViewById<TextView>(R.id.alarm_name)
        val alarmTimeBox = findViewById<TextView>(R.id.alarm_time)
        val alarmIsActiveSwitch = findViewById<Switch>(R.id.alarm_active_btn)
        dbHelper = DbHelper(context!!)
        alarmNameBox.text = alarm.name;
        alarmTimeBox.text = alarm.time.hours.toString() + ":" + alarm.time.minutes.toString()
        alarmIsActiveSwitch.isChecked = alarm.active
        alarm_active_btn.setOnClickListener(this)
        delete_alarm_btn.setOnClickListener(this)
        edit_alarm_btn.setOnClickListener(this)
    }
}