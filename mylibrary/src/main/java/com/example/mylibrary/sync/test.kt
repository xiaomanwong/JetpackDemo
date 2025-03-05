package com.example.mylibrary.sync

import android.os.Build
import androidx.annotation.RequiresApi

//import android.text.TextUtils
//import android.view.View
//import androidx.core.util.Pair
//import org.json.JSONObject
//import java.util.regex.Pattern
//
///**
// * @author Quinn
// * @date 2022/10/6
// * @Desc
// */
//class test {
//
//}
//
//
//fun main() {
//
//    val PARAM = "BF\\[\\[(\\[?.*?\\]?)\\]\\]"
//    val REQUEST_URL_PARTNER = Pattern.compile(PARAM)
//    val matcher = REQUEST_URL_PARTNER.matcher("contentValue")
//    push(
//        "contentValue",
//        mutableMapOf("xxx" to "123", "yyy" to "456", "zzz" to "789"),
//    )
//
//    try {
//        if (matcher.find()) {
//            val matcher0 = matcher.group()
//            val matcher1 = matcher.group(1)
//            println("match0:$matcher0")
//            println("match1:$matcher1")
//            if (TextUtils.isEmpty(matcher0) || TextUtils.isEmpty(matcher1)) {
//                ""
//            } else {
//                matcher1
//            }
//        } else {
//            println("no match")
//        }
//    } catch (e: Exception) {
//        ""
//    }
//}
//
//fun push(
//    routePath: String,
//    params: MutableMap<String, Any> = mutableMapOf(),
//    vararg sharedElement: Pair<View, String> = emptyArray()
//) {
//    println(
//        """
//                routePath = $routePath
//                params = %s
//                sharedElement = ${sharedElement.toString()}"
//            """.trimIndent().format(
//            """
//                    ${params.toString()}
//            }
//                """.trimIndent()
//        )
//    )
//}


fun isEmoji(codePoint: Int): Boolean {
//    val codePoint = character.code

//    // 检查是否是代理对
//    if (Character.isSurrogate(character)) {
//        return false
//    }

    // 常见 Emoji 范围
    return (codePoint in 0x1F600..0x1F64F) ||  // Emoticons
            (codePoint in 0x1F300..0x1F5FF) ||  // Miscellaneous Symbols and Pictographs
            (codePoint in 0x1F680..0x1F6FF) ||  // Transport and Map Symbols
            (codePoint in 0x1F700..0x1F77F) ||  // Alchemical Symbols
            (codePoint in 0x2600..0x26FF) ||    // Miscellaneous Symbols
            (codePoint in 0x2700..0x27BF) ||    // Dingbats
            (codePoint in 0x1F900..0x1F9FF) ||  // Supplemental Symbols and Pictographs
            (codePoint in 0x1FA70..0x1FAFF) ||  // Symbols and Pictographs Extended-A
            (codePoint in 0x1F780..0x1F7FF)     // Geometric Shapes Extended
}

// 从文本中提取 Emoji
fun extractEmojis(text: String): String {
    val emojiBuilder = StringBuilder()
    var i = 0
    while (i < text.length) {
        val codePoint = text.codePointAt(i)
        if (isEmoji(codePoint)) {
            emojiBuilder.append(Character.toChars(codePoint))
        }
        i += Character.charCount(codePoint)
    }
    return emojiBuilder.toString()
}

@RequiresApi(Build.VERSION_CODES.N)
fun main() {
    val text = "😊"
    val emojis = extractEmojis(text)

    // 输出识别出的所有 Emoji
    println("Emojis found: $emojis")
}