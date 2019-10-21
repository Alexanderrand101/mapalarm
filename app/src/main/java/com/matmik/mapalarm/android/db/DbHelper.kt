package com.matmik.mapalarm.android.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.matmik.mapalarm.android.model.Alarm
import com.matmik.mapalarm.android.model.Options
import com.matmik.mapalarm.android.toInt
import java.lang.ArithmeticException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class DbHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        val DB_NAME = "MapAlarm.db"
        val DB_VERSION = 1
        val SEPARATOR = "|"
        val NOT_FOUND_MSG = "alarm with id = %d not found"
        val TABLE_NAME = "alarms"
        val COL_ID = "id"
        val COL_NAME = "name"
        val COL_TIME = "time"
        val COL_OPTIONS = "options"
        val COL_ACTIVE = "active"
        val COL_LOCATION_BOUND = "location_bound"
        val COL_LOCATION = "location"
        var COL_DESCRIPTION = "description"
        val SCRIPT_CREATE = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_NAME TEXT NOT NULL, " +
                "$COL_TIME INTEGER NOT NULL, " +
                "$COL_OPTIONS TEXT NOT NULL, " +
                "$COL_ACTIVE INTEGER NOT NULL, " +
                "$COL_LOCATION_BOUND INTEGER NOT NULL, " +
                "$COL_LOCATION TEXT NOT NULL" +
                "$COL_DESCRIPTION TEXT NOT NULL" +
                ");"
        val SCRIPT_DROP = "DROP TABLE $TABLE_NAME;"
        val SCRIPT_GET = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = ?;"
        val SCRIPT_GET_ALL = "SELECT * FROM $TABLE_NAME;"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SCRIPT_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SCRIPT_DROP)
        onCreate(db)
    }

    fun getAlarm(id: Int): Alarm{
        val db = this.readableDatabase
        val cursor = db.rawQuery(SCRIPT_GET, arrayOf(id.toString()))
        if(cursor.moveToFirst()){
            return parseCursor(cursor)
        }
        else{
            throw Exception(NOT_FOUND_MSG.format(id))
        }

    }

    fun addAlarm(alarm: Alarm){
        val db = this.writableDatabase
        val alarmValues = ContentValues()
        alarmValues.put(COL_NAME, alarm.name)
        alarmValues.put(COL_TIME, alarm.time.time)
        alarmValues.put(COL_OPTIONS, alarm.options.joinToString(SEPARATOR))
        alarmValues.put(COL_ACTIVE, alarm.active.toInt())
        alarmValues.put(COL_LOCATION_BOUND, alarm.locationBound.toInt())
        alarmValues.put(COL_LOCATION, alarm.location)
        alarmValues.put(COL_DESCRIPTION, alarm.description)
        db.insert(TABLE_NAME, null, alarmValues)
    }

    fun updateAlarm(alarm: Alarm){
        val db = this.writableDatabase
        val alarmValues = ContentValues()
        alarmValues.put(COL_NAME, alarm.name)
        alarmValues.put(COL_TIME, alarm.time.time)
        alarmValues.put(COL_OPTIONS, alarm.options.joinToString(SEPARATOR))
        alarmValues.put(COL_ACTIVE, alarm.active.toInt())
        alarmValues.put(COL_LOCATION_BOUND, alarm.locationBound.toInt())
        alarmValues.put(COL_LOCATION, alarm.location)
        alarmValues.put(COL_DESCRIPTION, alarm.description)
        db.update(TABLE_NAME, alarmValues, "$COL_ID = ?", arrayOf(alarm.id.toString()))
    }

    fun deleteAlarm(id: Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun deleteAlarm(alarm: Alarm){
        deleteAlarm(alarm.id)
    }

    private fun parseCursor(cursor: Cursor): Alarm{
        return Alarm(cursor.getInt(cursor.getColumnIndex(COL_ID)),
            cursor.getString(cursor.getColumnIndex(COL_NAME)),
            Date(cursor.getLong(cursor.getColumnIndex(COL_TIME))),
            cursor.getString(cursor.getColumnIndex(COL_OPTIONS)).split(SEPARATOR).map{Options.valueOf(it)} as MutableList<Options>,
            cursor.getInt(cursor.getColumnIndex(COL_ACTIVE)) == 1,
            cursor.getInt(cursor.getColumnIndex(COL_LOCATION_BOUND)) == 1,
            cursor.getString(cursor.getColumnIndex((COL_LOCATION)))
        )
    }

    fun getAllAlarms():ArrayList<Alarm>{
        val db = this.readableDatabase
        val cursor = db.rawQuery(SCRIPT_GET_ALL, null)
        val alarmList = ArrayList<Alarm>()
        while (cursor.moveToNext())
            alarmList.add(parseCursor(cursor))
        return alarmList
    }
}