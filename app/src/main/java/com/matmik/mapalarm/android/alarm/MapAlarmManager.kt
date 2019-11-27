package com.matmik.mapalarm.android.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.matmik.mapalarm.android.db.DbHelper
import com.matmik.mapalarm.android.model.Alarm
import com.matmik.mapalarm.android.model.Options
import java.util.*

object MapAlarmManager {

    fun schedule(context: Context, alarm: Alarm){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent : PendingIntent =
            Intent(context, MapAlarmReciver::class.java)
                .let { intent ->
                    intent.putExtra("alarmName", alarm.name)
                    intent.putExtra("alarmId", alarm.id)
                    if (alarm.locationBound) intent.putExtra("alarmLoc", alarm.location)
                    PendingIntent.getBroadcast(context, alarm.id.toInt(), intent, PendingIntent.FLAG_CANCEL_CURRENT)
                }
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, alarm.time.hours)
            set(Calendar.MINUTE, alarm.time.minutes)
            set(Calendar.SECOND, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }else{
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
    }

    fun cancel(context: Context, alarm: Alarm){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent : PendingIntent? =
            Intent(context, MapAlarmReciver::class.java)
                .let { intent -> PendingIntent.getBroadcast(context, alarm.id.toInt(), intent, PendingIntent.FLAG_NO_CREATE) }
        if (alarmIntent != null)
            alarmManager.cancel(alarmIntent)
    }

    fun scheduleAllTodayAlarms(context: Context, dbHelper: DbHelper){
        val alarmList = dbHelper.getAllAlarms()
        val calendar = Calendar.getInstance()
        if (calendar.get(Calendar.HOUR_OF_DAY) == 23 && calendar.get(Calendar.MINUTE) == 59){
            calendar.add(Calendar.DAY_OF_WEEK, 1)
            val option = Options.calendarDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
            alarmList.filter { it.active && it.options.contains(option) }.forEach { schedule(context, it) }
        }
        else{
            alarmList.filter { it.evaluateAlarm(context) }.forEach { schedule(context, it) }
        }
    }

    fun Alarm.evaluateAlarm(context: Context): Boolean {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val option = Options.calendarDayOfWeek(dayOfWeek)
        val minDate = Date(0).apply {
            hours = hour
            minutes = minute
        }
        return this.active && this.time.after(minDate) && this.options.contains(option)
    }

    fun rescheduleAlarm(context: Context, alarm: Alarm){
        cancel(context, alarm)
        if(alarm.evaluateAlarm(context))
            schedule(context, alarm)
    }

    fun addAndSchedule(context: Context, alarm: Alarm, dbHelper: DbHelper):Alarm{
        var insertedAlaram = dbHelper.getAlarm(dbHelper.addAlarm(alarm))
        rescheduleAlarm(context, insertedAlaram)
        return insertedAlaram
    }

    fun updateAndSchedule(context: Context, alarm: Alarm, dbHelper: DbHelper){
        dbHelper.updateAlarm(alarm)
        rescheduleAlarm(context, alarm)
    }

    fun deleteAndUnschedule(context: Context, alarm: Alarm, dbHelper: DbHelper){
        dbHelper.deleteAlarm(alarm)
        cancel(context, alarm)
    }

    fun scheduleNextScheduler(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent : PendingIntent =
            Intent(context, MapAlarmSchedulerReciver::class.java)
                .let { intent -> PendingIntent.getBroadcast(context, 0, intent, 0) }
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }else{
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
    }
}