package com.luoyu.camellia

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luoyu.camellia.activities.BaseActivity
import com.luoyu.camellia.activities.ModuleIntroductionActivity
import com.luoyu.camellia.adapters.MainActivityLayoutAdapter
import com.luoyu.camellia.databinding.ActivityMainBinding
import com.luoyu.camellia.utils.IntentUtil
import com.luoyu.utils.Update
import com.luoyu.utils.FileUtil
import java.io.File

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private val itemList = arrayListOf(
            MainActivityLayoutAdapter.Item("Camellia(${BuildConfig.VERSION_NAME})") { },
            MainActivityLayoutAdapter.Item("模块介绍") {
                val intent = Intent(act, ModuleIntroductionActivity::class.java)
                act.startActivity(intent)
            },
            MainActivityLayoutAdapter.Item("交流讨论") {
                val items = arrayOf("加入QQ聊天群", "加入QQ通知群")
                MaterialAlertDialogBuilder(act)
                    .setTitle("模块介绍")
                    .setItems(items) { _, which ->
                        when (which) {
                            0 -> IntentUtil.openQQGroup(act, "902327702")
                            1 -> IntentUtil.openQQGroup(act, "837012640")
                        }
                    }
                    .setPositiveButton("取消", null)
                    .show()
            },
            MainActivityLayoutAdapter.Item("检测更新") {
            Update.create(act)
            // Clean up cache
            FileUtil.deleteFile(File("/storage/emulated/0/Android/data/com.luoyu.camellia/cache"))
            },
            MainActivityLayoutAdapter.Item("查看作者哔哩哔哩动态") {
                IntentUtil.openBilibili(act)
            }
        )
        private lateinit var act: Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(layoutInflater)

        act = this
        // set content view to binding's root
        setContentView(binding.root)

        // Init recyclerview
        val recycler: RecyclerView = binding.root.getChildAt(0) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = MainActivityLayoutAdapter(itemList)

        // Set recyclerView's background
        val recyclerViewBackground = GradientDrawable().apply {
            setColor(Color.parseColor("#F5F5F5"))
            cornerRadius = 30f
            alpha = 130
        }

        recycler.background = recyclerViewBackground
        recycler.y = -500f
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
