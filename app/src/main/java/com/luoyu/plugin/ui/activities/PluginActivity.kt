package com.luoyu.plugin.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luoyu.camellia.R
import com.luoyu.camellia.activities.SettingsActivity
import com.luoyu.camellia.activities.support.BaseActivity

class PluginActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.plugin_activity)

        // Hide navigation bar (assuming method exists)
        requestHideNavigationBar()
        
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.plugin_activity_recyclerView)
        initRecycleView()
    }

    /*
     * 嘿嘿嘿
     */
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun initRecycleView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        var pluginPath = com.luoyu.utils.PathUtil.getApkDataPath()
        pluginPath = "${pluginPath}plugin"
        val root: java.io.File = java.io.File(pluginPath)
        if(root.exists() && root.isDirectory) {
          val pluginItems = root.listFiles()
          
        }
    }

}
