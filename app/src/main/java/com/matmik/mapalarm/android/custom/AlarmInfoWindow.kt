package com.matmik.mapalarm.android.custom

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.matmik.mapalarm.android.R
import com.matmik.mapalarm.android.alarm.MapAlarmManager
import com.matmik.mapalarm.android.db.DbHelper
import com.matmik.mapalarm.android.model.Alarm
import kotlinx.android.synthetic.main.alarm_info_window.*
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow

class AlarmInfoWindow(resId: Int, val refreshableContainer: RefreshableContainer?, val map: MapView, val alarm:Alarm):
    InfoWindow(resId, map), View.OnClickListener {

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.delete_alarm_btn -> {
                MapAlarmManager.deleteAndUnschedule(v.context.applicationContext, alarm, DbHelper(map.context.applicationContext))
                var viewSt = v;
                if (refreshableContainer == null) {
                    val marker =
                        map.overlays.find { it is Marker && it.infoWindow == this } as Marker
                    marker.infoWindow.close()
                    map.overlays.remove(map.overlays.find { it is Marker && it.infoWindow == this })
                }
                refreshableContainer?.refreshTb(alarm)
            }
        }
    }

    override fun onOpen(item: Any?) {
        closeAllInfoWindowsOn(map)
        mView.findViewById<CardView>(R.id.info_card).setOnClickListener(this)
        mView.findViewById<ImageButton>(R.id.delete_alarm_btn).setOnClickListener(this)
        mView.findViewById<TextView>(R.id.alarm_name).text = alarm.name
        mView.findViewById<TextView>(R.id.alarm_time).text = alarm.time.hours.toString() + ":" + alarm.time.minutes.toString()
    }

    override fun onClose() {

    }

}