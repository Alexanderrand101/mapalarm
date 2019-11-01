package com.matmik.mapalarm.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import com.matmik.mapalarm.android.db.DbHelper
import com.matmik.mapalarm.android.model.Alarm
import com.matmik.mapalarm.android.model.Options
import java.text.SimpleDateFormat
import android.widget.TimePicker
import android.app.TimePickerDialog
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.activity_edit_note.*
import java.text.DateFormat


class EditNoteActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var alarm: Alarm
    private lateinit var dbHelper: DbHelper

    private var mHour: Int = 0
    private var mMinute: Int = 0


    private lateinit var name: EditText
    private lateinit var time :EditText

    private lateinit var monday: CheckBox

    private lateinit var tuesday: CheckBox

    private lateinit var wednesday: CheckBox

    private lateinit var thursday : CheckBox

    private lateinit var friday: CheckBox

    private lateinit var saturday: CheckBox

    private lateinit var sunday: CheckBox

    private lateinit var description: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)


        name =findViewById(R.id.name)
        time = findViewById(R.id.time)

        monday = findViewById<CheckBox>(R.id.monday)

        tuesday = findViewById<CheckBox>(R.id.tuesday)

        wednesday = findViewById<CheckBox>(R.id.wednesday)

        thursday = findViewById<CheckBox>(R.id.thursday)

        friday = findViewById<CheckBox>(R.id.friday)

        saturday = findViewById<CheckBox>(R.id.saturday)

        sunday = findViewById<CheckBox>(R.id.sunday)

        description=findViewById(R.id.description)




        dbHelper = DbHelper(this)
        alarm = intent.getSerializableExtra("EditableNote") as Alarm
        name.setText(alarm.name)
        time.setOnClickListener(this)
        time.setText(SimpleDateFormat("HH : mm").format(alarm.time))

        monday.isChecked = alarm.options.contains(Options.Monday)
        tuesday.isChecked = alarm.options.contains(Options.Tuesday)
        wednesday.isChecked = alarm.options.contains(Options.Wednesday)
        thursday.isChecked = alarm.options.contains(Options.Thursday)
        friday.isChecked = alarm.options.contains(Options.Friday)
        saturday.isChecked = alarm.options.contains(Options.Saturday)
        sunday.isChecked = alarm.options.contains(Options.Sunday)

        findViewById<Switch>(R.id.active).isChecked = alarm.active
        findViewById<Switch>(R.id.located).isChecked = alarm.locationBound

        description.setText(alarm.description)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.save -> {
                alarm.name = name.text.toString()
                alarm.options.clear()
                if (monday.isChecked) alarm.options.add(Options.Monday)
                if (tuesday.isChecked) alarm.options.add(Options.Tuesday)
                if (wednesday.isChecked) alarm.options.add(Options.Wednesday)
                if (thursday.isChecked) alarm.options.add(Options.Thursday)
                if (friday.isChecked) alarm.options.add(Options.Friday)
                if (saturday.isChecked) alarm.options.add(Options.Saturday)
                if (sunday.isChecked) alarm.options.add(Options.Sunday)


                alarm.time = SimpleDateFormat("HH : mm").parse(time.text.toString())



                alarm.active = findViewById<Switch>(R.id.active).isChecked
                alarm.locationBound = findViewById<Switch>(R.id.located).isChecked

                alarm.description = description.text.toString()
                if (alarm.id >= 0)
                    dbHelper.updateAlarm(alarm)
                else
                    dbHelper.addAlarm(alarm)
                val editIntent = Intent(this, MainActivity::class.java)
                startActivity(editIntent)
            }
            R.id.time -> {
                callTimePicker()
            }
        }

    }


    private fun callTimePicker() {
        // получаем текущее время
        val cal = Calendar.getInstance()
        mHour = cal.get(Calendar.HOUR_OF_DAY)
        mMinute = cal.get(Calendar.MINUTE)

        // инициализируем диалог выбора времени текущими значениями
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val editTextTimeParam = "$hourOfDay : $minute"
                time.setText(editTextTimeParam)
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

}
