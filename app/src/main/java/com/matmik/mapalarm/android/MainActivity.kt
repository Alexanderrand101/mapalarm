package com.matmik.mapalarm.android

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.matmik.mapalarm.android.config.LocaleManager
import com.matmik.mapalarm.android.custom.AlarmCard
import com.matmik.mapalarm.android.custom.AlarmListFragment
import com.matmik.mapalarm.android.custom.MapFragment
import com.matmik.mapalarm.android.custom.OptionsFragment
import com.matmik.mapalarm.android.db.DbHelper
import com.matmik.mapalarm.android.model.Alarm
import com.matmik.mapalarm.android.model.Options
import com.matmik.mapalarm.android.ui.login.LoginActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var dbHelper: DbHelper
    private var counter = 0

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)
        dbHelper = DbHelper(this)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
        /*resetTitle()
        this.applicationContext.resources.configuration
        this.baseContext.resources.configuration
        val string = getString(R.string.hw);*/
        //fab.setOnClickListener(this)
        //add.setOnClickListener(this)
        //update.setOnClickListener(this)
        //delete.setOnClickListener(this)
        //refreshTb()
    }

    fun resetTitle(){
        val info = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
        if (info.labelRes != 0)
            setTitle(info.labelRes)
    }

    //override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu)
        //return true
    //}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.signOutButton -> {
                FirebaseAuth.getInstance().signOut()
                val start = Intent(this, LoginActivity::class.java)
                startActivity(start)
                true}
//            R.id.verifyEmailButton -> {
//                FirebaseAuth.getInstance().signOut()
//                val start = Intent(this, LoginActivity::class.java)
//                startActivity(start)
//                true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

           //R.id.fab -> {
                //LocaleManagerMew.setLocale(this@LoginCustomerFragment.activity?.applicationContext)
                /*var mCurrentLanguage = LocaleManager.getCurrentLanguage(this.applicationContext)
                if (mCurrentLanguage == LocaleManager.mRussianFlag) {
                    LocaleManager.setNewLocale(this.applicationContext, LocaleManager.mEnglishFlag)
                } else if (mCurrentLanguage == LocaleManager.mEnglishFlag) {
                    LocaleManager.setNewLocale(this.applicationContext, LocaleManager.mRussianFlag)
                }
                this.recreate()*/
            //}
            R.id.add_alarm_btn -> {
                /*dbHelper.addAlarm(Alarm(name = "alarm$counter",
                    time = Date(System.currentTimeMillis()),
                    options = mutableListOf(Options.Monday),
                    active = true,
                    locationBound = false,
                    location = "dot$counter",
                    description = ""))
                counter++*/
                val editIntent = Intent(this, EditNoteActivity::class.java)
                editIntent.putExtra("EditableNote", Alarm(name = "new Alarm",
                    time = Date(0),
                    options = mutableListOf(Options.Monday)))
                startActivity(editIntent)
            }
            /*R.id.update -> {
                /*val editIntent=Intent(this,EditNoteActivity::class.java)
                editIntent.putExtra("EditableNote",dbHelper.getAllAlarms().maxBy { it.id })
                startActivity(editIntent)

            }*/
            /*R.id.delete -> {
                val id = dbHelper.getAllAlarms().map { it.id }.maxBy { it }
                dbHelper.deleteAlarm(id!!)
            }*/*/
            R.id.signOutButton -> {
                FirebaseAuth.getInstance().signOut()
                val start = Intent(this, LoginActivity::class.java)
                startActivity(start)
            }
            R.id.imageButton ->{
                val dialog = AlarmListFragment()
                dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme)
                dialog.show(supportFragmentManager, "dialog")
            }
            R.id.imageButton2 -> {
                val dialog = OptionsFragment()
                //dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme)
                dialog.show(supportFragmentManager, "dialog")
            }
        }
        refreshTb()
    }

    private fun refreshTb(){
        /*val view = findViewById<TextView>(R.id.output)
        var text = ""
        for (alarm in dbHelper.getAllAlarms())
            text += alarm.toString() + "\n"
        view.text = text*/
        /*val layout = findViewById<LinearLayout>(R.id.lineartest)
        layout.removeAllViews()
        for (alarm in dbHelper.getAllAlarms()){
            val alarmCard = AlarmCard(alarm, this)
            alarmCard.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300)
            layout.addView(alarmCard)
        }*/
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val alarmListFragment =
                supportFragmentManager.findFragmentById(R.id.alarm_list_fragment) as AlarmListFragment
            alarmListFragment.refreshTb(null)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            0 -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
                    mapFragment.centerOnMe()
                } else {
                    Toast.makeText(this, "location based alarm won't work", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
