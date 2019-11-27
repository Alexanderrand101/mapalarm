package com.matmik.mapalarm.android

import com.matmik.mapalarm.android.model.Alarm
import com.matmik.mapalarm.android.model.Options
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

class AwesomeTest1 {
        @Test
        fun  check_isCorrect() {
            val testAlarm = Alarm(name = "new Alarm", time = Date(0), options = mutableListOf(Options.Monday))
            assertEquals("Thu Jan 01 04:00:00 SAMT 1970", testAlarm.time.toString())
            assertEquals("new Alarm", testAlarm.name)
            assertEquals("[Monday]", testAlarm.options.toString())
        }
}