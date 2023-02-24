package com.example.mylibrary.sync

import org.json.JSONObject
import org.json.JSONTokener

/**
 * @author Quinn
 * @date 2022/10/6
 * @Desc
 */
class test {

}


fun main() {

    val str = "\"{\n" +
            "\"trans_info\": \"{\\\"message_id\\\":\\\"test:742353130_5\\\",\\\"push_channel\\\":\\\"fcm-self\\\",\\\"push_type\\\":5,\\\"userid\\\":\\\"8ojWDZgbLa9e\\\"}\",\n" +
            "\"_notification\": \"{\\\"_body\\\":\\\"push content\\\",\\\"_title\\\":\\\"test push\\\"}\",\n" +
            "\"extra\": \"{\\\"action\\\":\\\"{\\\\\\\"action\\\\\\\":\\\\\\\"push\\\\\\\",\\\\\\\"badge\\\\\\\":{\\\\\\\"all\\\\\\\":1,\\\\\\\"feed_badge\\\\\\\":0,\\\\\\\"friend_badge\\\\\\\":0},\\\\\\\"options\\\\\\\":false,\\\\\\\"parameters\\\\\\\":{\\\\\\\"is_official\\\\\\\":2,\\\\\\\"nickname\\\\\\\":\\\\\\\"软桃真好吃\\\\\\\",\\\\\\\"profile\\\\\\\":\\\\\\\"https://ibz2.go2yd.com/image.php?url=ZL_cnt_2_01Durek2704M×tamp=1661494254&auth_id=truelyimg&auth_key=530b2a6a39e6505347d8bb518641c0f3\\\\\\\",\\\\\\\"userid\\\\\\\":\\\\\\\"8ojWDZgbLa9e\\\\\\\"},\\\\\\\"path\\\\\\\":\\\\\\\"conversation_page\\\\\\\"}\\\"}\"\n" +
            "}\""


    val newStr = str.replace("\\", "").replace("\"{", "{").replace("}\"", "}").replace("\n","").replace("\"", "'")
//        .replace("\"", "/\"")
    println(newStr)

    val json = JSONObject(JSONTokener(newStr))

    println(json.toString())
}