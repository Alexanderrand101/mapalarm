package com.matmik.mapalarm.android.config

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Build.VERSION_CODES.N
import android.os.LocaleList
import android.preference.PreferenceManager
import java.util.*

object LocaleManager {

    val SELECTED_LANGUAGE = "CURRENT_USER_LANGUAGE"
    var mSharedPreference: SharedPreferences? = null

    var mEnglishFlag = "en"
    var mRussianFlag = "ru"

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String?, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }

    }

    inline operator fun <reified T : Any> SharedPreferences.get(key: String?, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun setLocale(context: Context?): Context {
        return updateResources(context!!, getCurrentLanguage(context)!!)
    }

    inline fun setNewLocale(context: Context, language: String) {

        persistLanguagePreference(context, language)
        updateResources(context, language)
    }

    inline fun getCurrentLanguage(context: Context?): String? {

        var mCurrentLanguage: String?

        if (mSharedPreference == null)
            mSharedPreference = defaultPrefs(context!!)

        mCurrentLanguage = mSharedPreference!!.get(SELECTED_LANGUAGE, mEnglishFlag);

        return mCurrentLanguage
    }

    fun persistLanguagePreference(context: Context, language: String) {
        if (mSharedPreference == null)
            mSharedPreference = defaultPrefs(context)

        mSharedPreference!![SELECTED_LANGUAGE] = language

    }


    @TargetApi(17)
    fun updateResources(context: Context, language: String): Context {

        var contextFun = context

        var locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration

        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale)
            contextFun = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
        return contextFun
    }

    fun defaultPrefs(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
}