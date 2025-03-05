package com.example.myapplication

import android.text.format.DateFormat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        println( DateFormat.getLongDateFormat(appContext).format(System.currentTimeMillis()))

        val a = 60_60_000
        val b = 60 * 60 * 1000
        assertEquals(a, b)

//        assertEquals("com.example.myapplication", appContext.packageName)
    }
}
