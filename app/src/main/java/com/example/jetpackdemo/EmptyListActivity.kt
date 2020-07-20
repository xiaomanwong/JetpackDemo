package com.example.jetpackdemo

import android.app.Activity
import android.os.Bundle
import android.widget.ListView

class EmptyListActivity : Activity() {

    private lateinit var emptyList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_listview)

        emptyList = findViewById(R.id.empty_list_view)

        emptyList.emptyView = findViewById(R.id.empty_layout)
    }

}