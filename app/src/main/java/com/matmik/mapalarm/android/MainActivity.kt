package com.matmik.mapalarm.android

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.matmik.mapalarm.android.config.LocaleManager

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        this.applicationContext.resources.configuration
        fab.setOnClickListener(this)
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
                var mCurrentLanguage = LocaleManager.getCurrentLanguage(this.applicationContext)
                if (mCurrentLanguage == LocaleManager.mRussianFlag) {
                    LocaleManager.setNewLocale(this.applicationContext, LocaleManager.mEnglishFlag)
                } else if (mCurrentLanguage == LocaleManager.mEnglishFlag) {
                    LocaleManager.setNewLocale(this.applicationContext, LocaleManager.mRussianFlag)
                }
                this.recreate()
            }
        }
    }
}
