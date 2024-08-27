package com.luoyu.camellia.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.luoyu.camellia.R
import com.luoyu.camellia.model.SettingOpt
import com.luoyu.camellia.utils.showToast
import com.luoyu.camellia.base.MItem
import com.luoyu.camellia.base.UiClickHandler

// 设置项适配器
class SettingsItemAdapter(val context: Context, val settingItemList: List<SettingOpt>): RecyclerView.Adapter<SettingViewHolder>() {

    // 获取每个item的类型
    override fun getItemViewType(position: Int): Int {
        return settingItemList[position].type
    }


    // 创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        lateinit var viewHolder: SettingViewHolder
        when(viewType) {
            SettingOpt.TYPE_ITEM -> {
                // 创建item类型的ViewHolder
                val view =
                    LayoutInflater.from(context).inflate(R.layout.setting_item, parent, false)
                viewHolder = SettingItemViewHolder(view)
            }
            SettingOpt.TYPE_SWITCH -> {
                // 创建switch类型的ViewHolder
                val view =
                    LayoutInflater.from(context).inflate(R.layout.setting_switch, parent, false)
                viewHolder = SettingSwitchViewHolder(view)

                viewHolder.itemView.setOnClickListener {
                    viewHolder.switch.setChecked(!viewHolder.switch.isChecked)
                }
            }
            else -> {
                // 抛出异常
                throw RuntimeException("Error setting item type!")
            }
        }
        return viewHolder
    }

    // 获取item的数量
    override fun getItemCount() = settingItemList.size

    // 绑定ViewHolder
    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        val item_name = settingItemList[position].text
        when(holder){
            is SettingItemViewHolder -> {
                holder.settingItemName.text= item_name
                holder.cardView.setOnClickListener {
                        UiClickHandler.onClick(item_name)
                    }
                    holder.cardView.setOnLongClickListener {
                        UiClickHandler.onLongClick(item_name)
                        true
                    }
                }
            is SettingSwitchViewHolder -> {
                holder.settingSwitchName.text = item_name
                val boo: Boolean = MItem.Config.getBooleanData("${item_name}/开关",false)
                holder.switch.setChecked(boo)
                holder.switch.setOnCheckedChangeListener { _ , isChecked ->
                    MItem.Config.putData("${item_name}/开关",isChecked)
                    if(isChecked) UiClickHandler.onSwitchClick(item_name)
                }
                holder.cardView.setOnLongClickListener {
                    UiClickHandler.onLongClick(item_name)
                    true
                }
            }
            
        }

    }

}