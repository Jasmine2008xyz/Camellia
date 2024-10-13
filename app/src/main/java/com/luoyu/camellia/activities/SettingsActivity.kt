package com.luoyu.camellia.activities

import android.content.Context
import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luoyu.camellia.R
import com.luoyu.camellia.activities.support.BaseActivity
import com.luoyu.camellia.adapters.SettingsItemAdapter
import com.luoyu.xposed.ModuleController
import com.luoyu.camellia.model.SettingOpt
import com.luoyu.camellia.utils.showToast
import com.luoyu.utils.Util
import com.luoyu.utils.Update
import com.luoyu.utils.FileUtil
import com.luoyu.utils.PathUtil
import com.luoyu.utils.AppUtil
import com.luoyu.xposed.utils.QQUtil
import com.luoyu.xposed.data.module.HostInfo
import com.luoyu.camellia.utils.IntentUtil
import com.luoyu.camellia.activities.helper.ActivityAttributes
import com.luoyu.camellia.BuildConfig
import com.tencent.mobileqq.widget.QQToast
import java.io.File

class SettingsActivity: BaseActivity() {

    companion object {
     // 定义一个设置项列表
        val settingsItemList = ArrayList<SettingOpt>()
        
        init {
            settingsItemList.add(SettingOpt(settingsItemList.size,"图片外显",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"修改图片大小",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"长按消息菜单添加显示MsgRecord项",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"文件重命名apk.1",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"屏蔽链接信息",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"屏蔽拍一拍Timing",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"一键20赞",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"解锁卡片消息左滑",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"自动抢红包",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"阻止Java层闪退",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"长按文本修改内容(慎)",SettingOpt.TYPE_SWITCH))
            settingsItemList.add(SettingOpt(settingsItemList.size,"跳转网页功能",SettingOpt.TYPE_ITEM))
        }
        
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
         * 谷歌material3控件必须在Theme.AppCompat或其子类下
         * 所以这里要设置主题，否则抛出java.lang.IllegalArgumentException
         */
        setTheme(R.style.AppTheme)
        
        setContentView(R.layout.setting_activity_v2)
        
        requestHideNavigationBar()
        
        initUserInfo()
        
        ActivityAttributes.context = this
        
        // 获取toolbar
        val toolbar: Toolbar = findViewById(R.id.setting_v2_title_toolbar)
        // 获取RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.setting_v2_recyclerView)
        
    //    val image_view: AppCompatImageView = findViewById(R.id.setting_v2_background)
        // 初始化toolbar
        initToolBar(toolbar)
        
      //  initBackground(image_view)
        // 设置RecyclerView的布局管理器
        val layoutManager = LinearLayoutManager(this)
        // 设置RecyclerView的适配器
        val adapter = SettingsItemAdapter(this, settingsItemList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    // 初始化设置项列表
    @Deprecated(message = "弃用")
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
                R.id.setting_item_1 -> { // 交流讨论
                 joinGroup(toolbar.getContext())
                    true
                }
                R.id.setting_item_2 -> { // 更新日志
                 QQToast.makeText(this,5,"Clicked",0,0).show();
                    true
                }
                R.id.setting_item_3 -> { //检测更新
                    val a=Update.create(ActivityAttributes.context as Activity)
                    // Clean up cache
                    val file = File(PathUtil.getApkDataPath() + "cache")
                    if (file.exists()) {
                        FileUtil.deleteFile(file)
                    }
                    true
                }
                else -> false
            }
        }
    }
    @Deprecated(message = "暂时放弃")
    private fun initBackground(image: AppCompatImageView) {
        Glide.with(this)
                .load(R.drawable.settingbackground)
                .into(image);
    }
    
    private fun joinGroup(context: Context) {
    val items = arrayOf("加入QQ聊天群", "加入QQ通知群")
    MaterialAlertDialogBuilder(context)
    .setTitle("交流讨论")
    .setItems(items) { _, which ->
        when (which) {
            0 -> IntentUtil.openQQGroup(context, "902327702")
            1 -> IntentUtil.openQQGroup(context, "837012640")
            else -> {}
        }
    }
    .setPositiveButton("Leave", null)
    .show()
    }
    
    private fun initUserInfo() {
        val textview: TextView = findViewById(R.id.module_user_info_textview)
        val qqUin = QQUtil.getCurrentUin()
        val qqVersionName = AppUtil.getHostInfo(this)
        val moduleVersionName = BuildConfig.VERSION_NAME
        val moduleVersionCode = BuildConfig.VERSION_CODE
        textview.text = "-> 登录账号：${qqUin}\n-> 用户身份：普通用户\n-> QQ版本：${qqVersionName}\n-> 模块版本：${moduleVersionName}(${moduleVersionCode})\n-> 版本一言：月光太冷真的难入喉。"
    }
}