package com.luoyu.camellia.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luoyu.camellia.R
import com.luoyu.camellia.activities.support.BaseActivity
import com.luoyu.camellia.adapters.SettingsItemAdapter
import com.luoyu.camellia.model.SettingOpt
import com.luoyu.camellia.utils.showToast
import com.luoyu.camellia.utils.Util
import com.luoyu.camellia.base.MItem

class SettingsActivity: BaseActivity() {

    // 定义一个设置项列表
    private val settingsItemList = ArrayList<SettingOpt>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
         * 谷歌material3控件必须在Theme.AppCompat或其子类下
         * 所以这里要设置主题，否则抛出java.lang.IllegalArgumentException
         */
        setTheme(R.style.AppTheme)
        
        setContentView(R.layout.settings_activity)
        // 获取toolbar
        val toolbar: Toolbar = findViewById(R.id.setting_title_toolbar)
        // 获取RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.setting_recyclerView)
        // 初始化设置项列表
        initItems()
        // 初始化toolbar
        initToolBar(toolbar)
        // 设置RecyclerView的布局管理器
        val layoutManager = LinearLayoutManager(this)
        // 设置RecyclerView的适配器
        val adapter = SettingsItemAdapter(this, settingsItemList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    // 初始化设置项列表
    private fun initItems() {
        for(i in 1..20){
            // 创建一个设置项
            val settingOpt = SettingOpt(i, "This is a test Item $i", (0..1).random())
            // 将设置项添加到列表中
            settingsItemList.add(settingOpt)
        }
    }

    // 初始化toolbar
    private fun initToolBar(toolbar: Toolbar) {
        // 设置toolbar的导航按钮点击事件
        toolbar.setNavigationOnClickListener {
            finish()
        }
        // 设置toolbar的菜单项点击事件
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