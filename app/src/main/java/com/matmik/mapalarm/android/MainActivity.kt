package com.matmik.mapalarm.android

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.matmik.mapalarm.android.config.LocaleManager
import com.matmik.mapalarm.android.db.DbHelper
import com.matmik.mapalarm.android.model.Alarm
import com.matmik.mapalarm.android.model.Options

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
        setSupportActionBar(toolbar)
        dbHelper = DbHelper(this)
        /*resetTitle()
        this.applicationContext.resources.configuration
        this.baseContext.resources.configuration
        val string = getString(R.string.hw);*/
        fab.setOnClickListener(this)
        add.setOnClickListener(this)
        update.setOnClickListener(this)
        delete.setOnClickListener(this)
        refreshTb()
    }

    fun resetTitle(){
        val info = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
        if (info.labelRes != 0)
            setTitle(info.labelRes);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.fab -> {
                //LocaleManagerMew.setLocale(this@LoginCustomerFragment.activity?.applicationContext)
                /*var mCurrentLanguage = LocaleManager.getCurrentLanguage(this.applicationContext)
                if (mCurrentLanguage == LocaleManager.mRussianFlag) {
                    LocaleManager.setNewLocale(this.applicationContext, LocaleManager.mEnglishFlag)
                } else if (mCurrentLanguage == LocaleManager.mEnglishFlag) {
                    LocaleManager.setNewLocale(this.applicationContext, LocaleManager.mRussianFlag)
                }
                this.recreate()*/
            }
            R.id.add -> {
                dbHelper.addAlarm(Alarm(name = "alarm$counter",
                    time = Date(System.currentTimeMillis()),
                    options = arrayOf(Options.Monday).toList(),
                    active = true,
                    locationBound = false,
                    location = "dot$counter"))
                counter++
            }
            R.id.update -> {
                val alarm = dbHelper.getAllAlarms().maxBy { it.id }
                alarm!!.time = Date(System.currentTimeMillis())
                alarm!!.location = "dot$counter"
                dbHelper.updateAlarm(alarm)
                counter++
            }
            R.id.delete -> {
                val id = dbHelper.getAllAlarms().map { it.id }.maxBy { it }
                dbHelper.deleteAlarm(id!!)
            }
        }
        refreshTb()
    }

    private fun refreshTb(){
        val view = findViewById<TextView>(R.id.output)
        var text = ""
        for (alarm in dbHelper.getAllAlarms())
            text += alarm.toString() + "\n"
        view.text = text
    }
}
