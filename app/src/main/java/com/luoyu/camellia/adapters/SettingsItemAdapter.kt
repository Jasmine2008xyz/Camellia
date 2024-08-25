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

class SettingsItemAdapter(val context: Context, val settingItemList: List<SettingOpt>): RecyclerView.Adapter<SettingViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return settingItemList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        lateinit var viewHolder: SettingViewHolder
        when(viewType) {
            SettingOpt.TYPE_ITEM -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.setting_item, parent, false)
                viewHolder = SettingItemViewHolder(view)
                viewHolder.cardView.setOnClickListener {
                    "You click RecyclerView Item".showToast()
                }
            }
            SettingOpt.TYPE_SWITCH -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.setting_switch, parent, false)
                viewHolder = SettingSwitchViewHolder(view)
                viewHolder.itemView.setOnClickListener {
                    viewHolder.switch.setChecked(!viewHolder.switch.isChecked)
                }
                viewHolder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
                    "You changed switch to $isChecked".showToast()
                }
            }
            else -> {throw RuntimeException("Error setting item type!")}
        }

        return viewHolder
    }

    override fun getItemCount() = settingItemList.size

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        when(holder){
            is SettingItemViewHolder -> holder.settingItemName.text= settingItemList[position].text
            is SettingSwitchViewHolder -> holder.settingSwitchName.text = settingItemList[position].text
        }

    }

}