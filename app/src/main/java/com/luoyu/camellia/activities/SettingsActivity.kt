package com.luoyu.camellia.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luoyu.camellia.R
import com.luoyu.camellia.activities.support.BaseActivity
import com.luoyu.camellia.adapters.SettingsItemAdapter
import com.luoyu.camellia.utils.showToast

class SettingsActivity: AppCompatActivity() {

    val SettingsItemList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        val toolbar: Toolbar = findViewById(R.id.setting_title_toolbar)
        val recyclerView: RecyclerView = findViewById(R.id.setting_recyclerView)
        initItems()
        initToolBar(toolbar)
        val layoutManager = LinearLayoutManager(this)
        val adapter = SettingsItemAdapter(this, SettingsItemList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun initItems() {
        for(i in 0..19){
            SettingsItemList.add("This is a test Item $i")
        }
    }

    private fun initToolBar(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.item -> {
                    "You click Item".showToast()
                    true
                }
                else -> false
            }
        }
    }
}