package com.example.mylibrary.sync

import android.text.TextUtils
import android.view.View
import androidx.core.util.Pair
import org.json.JSONObject
import java.util.regex.Pattern

/**
 * @author Quinn
 * @date 2022/10/6
 * @Desc
 */
class test {

}


fun main() {

    val PARAM = "BF\\[\\[(\\[?.*?\\]?)\\]\\]"
    val REQUEST_URL_PARTNER = Pattern.compile(PARAM)
    val matcher = REQUEST_URL_PARTNER.matcher("contentValue")
    push(
        "contentValue",
        mutableMapOf("xxx" to "123", "yyy" to "456", "zzz" to "789"),
    )

    try {
        if (matcher.find()) {
            val matcher0 = matcher.group()
            val matcher1 = matcher.group(1)
            println("match0:$matcher0")
            println("match1:$matcher1")
            if (TextUtils.isEmpty(matcher0) || TextUtils.isEmpty(matcher1)) {
                ""
            } else {
                matcher1
            }
        } else {
            println("no match")
        }
    } catch (e: Exception) {
        ""
    }
}

fun push(
    routePath: String,
    params: MutableMap<String, Any> = mutableMapOf(),
    vararg sharedElement: Pair<View, String> = emptyArray()
) {
    println(
        """
                routePath = $routePath
                params = %s
                sharedElement = ${sharedElement.toString()}"
            """.trimIndent().format(
            """
                    ${params.toString()}
            }
                """.trimIndent()
        )
    )
}