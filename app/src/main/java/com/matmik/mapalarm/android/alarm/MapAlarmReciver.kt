package com.matmik.mapalarm.android.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import androidx.core.app.NotificationCompat
import com.matmik.mapalarm.android.R
import com.matmik.mapalarm.android.model.Alarm
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class MapAlarmReciver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        val alarmId = intent?.getLongExtra("alarmId", -1)?.toInt()
        val alarmName = intent?.getStringExtra("alarmName")
        val alarmLoc = intent?.getStringExtra("alarmLoc")
        if(alarmLoc == null || alarmLoc.isEmpty()) {
            val task = Task(
                pendingResult, context!!.applicationContext,
                alarmId,
                alarmName,
                null,
                null
            )
            task.execute()
        }
        else {
            val provider2 = GpsMyLocationProvider(context)
            provider2.addLocationSource(LocationManager.NETWORK_PROVIDER)
            val consumer = object : IMyLocationConsumer {
                override fun onLocationChanged(
                    location: Location?,
                    source: IMyLocationProvider?
                ) {
                    val task = Task(
                        pendingResult, context!!.applicationContext,
                        alarmId,
                        alarmName,
                        alarmLoc,
                        location
                    )
                    task.execute()
                    source?.stopLocationProvider()
                }
            }
            provider2.startLocationProvider(consumer)
        }
    }

    //это утечка памяти? вроде нет, несмотря на warning
    private class Task(
        private val pendingResult: PendingResult,
        private val context: Context,
        private val alarmId: Int?,
        private val alarmName: String?,
        private val alarmLoc: String?,
        private val location: Location?):
        AsyncTask<Void, Void, Unit>(){

        override fun doInBackground(vararg params: Void?){
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (alarmLoc != null && alarmLoc.isNotEmpty()){
                var res:FloatArray = FloatArray(10)
                val latid = alarmLoc.split(",")[0].toDouble()
                val longt = alarmLoc.split(",")[1].toDouble()
                val pointB = Location("B")
                pointB.latitude = latid
                pointB.longitude = longt
                val dist = location!!.distanceTo(pointB)
                if (dist < 100){
                    val mBuilder = NotificationCompat.Builder(context, "placeholder")
                        .setContentTitle("notification!")
                        .setContentText((alarmName ?: "null alarm sorry") + "; distance to location: " + dist.toString())
                        .setSmallIcon(R.drawable.ic_baseline_add)
                        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    notificationManager.notify(alarmId ?: -1, mBuilder.build())
                }
                //Location.distanceBetween(location!!.latitude, location!!.longitude, latid, longt, res)
                //var dist = res[0]
                //dist = 2.0F

            }else {
                val mBuilder = NotificationCompat.Builder(context, "placeholder")
                    .setContentTitle("notification!")
                    .setContentText(alarmName ?: "null alarm sorry")
                    .setSmallIcon(R.drawable.ic_baseline_add)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                notificationManager.notify(alarmId ?: -1, mBuilder.build())
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            pendingResult.finish()
        }

    }
}