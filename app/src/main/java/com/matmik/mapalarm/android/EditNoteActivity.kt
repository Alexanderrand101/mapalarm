package com.matmik.mapalarm.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.matmik.mapalarm.android.db.DbHelper
import com.matmik.mapalarm.android.model.Alarm
import com.matmik.mapalarm.android.model.Options


class EditNoteActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var alarm: Alarm
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        dbHelper = DbHelper(this)
        alarm = intent.getSerializableExtra("EditableNote") as Alarm
        findViewById<TextView>(R.id.name).text = alarm.name

        findViewById<CheckBox>(R.id.monday).isChecked = alarm.options.contains(Options.Monday)
        findViewById<CheckBox>(R.id.tuesday).isChecked = alarm.options.contains(Options.Tuesday)
        findViewById<CheckBox>(R.id.wednesday).isChecked = alarm.options.contains(Options.Wednesday)
        findViewById<CheckBox>(R.id.thursday).isChecked = alarm.options.contains(Options.Thursday)
        findViewById<CheckBox>(R.id.friday).isChecked = alarm.options.contains(Options.Friday)
        findViewById<CheckBox>(R.id.saturday).isChecked = alarm.options.contains(Options.Saturday)
        findViewById<CheckBox>(R.id.sunday).isChecked = alarm.options.contains(Options.Sunday)

        findViewById<Switch>(R.id.active).isChecked = alarm.active
        findViewById<Switch>(R.id.located).isChecked = alarm.locationBound
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.save -> {
                alarm.name = findViewById<TextView>(R.id.name).text.toString()
                alarm.options.clear()
                if (findViewById<CheckBox>(R.id.monday).isChecked) alarm.options.add(Options.Monday)
                if (findViewById<CheckBox>(R.id.tuesday).isChecked) alarm.options.add(Options.Tuesday)
                if (findViewById<CheckBox>(R.id.wednesday).isChecked) alarm.options.add(Options.Wednesday)
                if (findViewById<CheckBox>(R.id.thursday).isChecked) alarm.options.add(Options.Thursday)
                if (findViewById<CheckBox>(R.id.friday).isChecked) alarm.options.add(Options.Friday)
                if (findViewById<CheckBox>(R.id.saturday).isChecked) alarm.options.add(Options.Saturday)
                if (findViewById<CheckBox>(R.id.sunday).isChecked) alarm.options.add(Options.Sunday)

                alarm.active = findViewById<Switch>(R.id.active).isChecked
                alarm.locationBound = findViewById<Switch>(R.id.located).isChecked
            }
        }
        dbHelper.updateAlarm(alarm)
        val editIntent= Intent(this,MainActivity::class.java)
        startActivity(editIntent)
    }
}
