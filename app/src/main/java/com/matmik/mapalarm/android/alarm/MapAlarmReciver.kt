package com.matmik.mapalarm.android.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.core.app.NotificationCompat
import com.matmik.mapalarm.android.R
import com.matmik.mapalarm.android.model.Alarm

class MapAlarmReciver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        val task = Task(pendingResult, context!!.applicationContext, intent?.getSerializableExtra("alarm") as Alarm?)
        task.execute()
    }

    //это утечка памяти? вроде нет, несмотря на warning
    private class Task(
        private val pendingResult: PendingResult,
        private val context: Context,
        private val alarm: Alarm?):
        AsyncTask<Void, Void, Unit>(){

        override fun doInBackground(vararg params: Void?){
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mBuilder = NotificationCompat.Builder(context, "placeholder")
                .setContentTitle("notification!")
                .setContentText(alarm?.name ?: "null alarm sorry")
                .setSmallIcon(R.drawable.ic_baseline_add)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            notificationManager.notify(alarm?.id?.toInt() ?: -1, mBuilder.build())
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            pendingResult.finish()
        }

    }
}