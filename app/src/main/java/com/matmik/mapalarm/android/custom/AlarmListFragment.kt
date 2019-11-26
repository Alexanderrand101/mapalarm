package com.matmik.mapalarm.android.custom

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.matmik.mapalarm.android.R
import com.matmik.mapalarm.android.db.DbHelper
import com.matmik.mapalarm.android.model.Alarm

class AlarmListFragment: DialogFragment(),RefreshableContainer{

    private lateinit var dbHelper: DbHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setGravity(Gravity.TOP or Gravity.LEFT)
        dialog?.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.alarm_list_fragment, container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dbHelper = DbHelper(activity!!)
        refreshTb(null)
    }

    override fun refreshTb(alarm: Alarm?){
        /*val view = findViewById<TextView>(R.id.output)
        var text = ""
        for (alarm in dbHelper.getAllAlarms())
            text += alarm.toString() + "\n"
        view.text = text*/
        val layout = view!!.findViewById<LinearLayout>(R.id.contentRoot)
        val fragment = this.activity!!.supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        if (alarm != null && alarm.locationBound) fragment.refreshMarkers(alarm)
        layout.removeAllViews()
        for (alarm in dbHelper.getAllAlarms()){
            val alarmCard = AlarmCard(alarm, this, activity)
            alarmCard.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 8 }
            layout.addView(alarmCard)
        }
    }
}