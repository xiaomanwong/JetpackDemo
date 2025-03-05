package com.example.lib_common

/**
 * @author Quinn
 * @date 2024/9/30
 * @Desc
 */
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class PunctuationNormalizationTest {

    @Test
    fun testEnglishPunctuation() {
        val actual = numberFormatter.format(151.05431)
        val expected = "151"
        assertEquals(expected, actual)
    }

    @Test
    fun testChinesePunctuation() {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE)
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val input = "你好，世界！这是一个测试。"
        val expected = "你好，世界！这是一个测试。"
        val actual = PunctuationFormatter(appContext).normalizePunctuation(input)
        println(actual)
        assertEquals(expected, actual)
    }
}