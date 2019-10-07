package com.matmik.mapalarm.android

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.matmik.mapalarm.android.config.LocaleManager

open class ApplicationOverride : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.setLocale(this)
    }
}