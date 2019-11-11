package com.matmik.mapalarm.android.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import com.matmik.mapalarm.android.db.DbHelper
import java.util.*

class MapAlarmSchedulerReciver:  BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        val task = Task(pendingResult, context!!.applicationContext)//otherwise it is restricted
        task.execute()
    }

    //это утечка памяти? вроде нет, несмотря на warning
    private class Task(
        private val pendingResult: PendingResult,
        private val context: Context):
        AsyncTask<Void, Void, Unit>(){

        override fun doInBackground(vararg params: Void?){
            MapAlarmManager.scheduleAllTodayAlarms(context, dbHelper = DbHelper(context))
            MapAlarmManager.scheduleNextScheduler(context)
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            pendingResult.finish()
        }

    }
}